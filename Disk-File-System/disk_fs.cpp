#include <iostream>
#include <vector>
#include <map>
#include <assert.h>
#include <string.h>
#include <math.h>
#include <sys/types.h>
#include <unistd.h>
#include <sys/stat.h>
#include <fcntl.h>

using namespace std;

#define DISK_SIZE 256

// ============================================================================
void decToBinary(int n, char &c) {
    // array to store binary number
    int binaryNum[8];
    // counter for binary array
    int i = 0;
    while (n > 0) {
        // storing remainder in binary array
        binaryNum[i] = n % 2;
        n = n / 2;
        i++;
    }
    // printing binary array in reverse order
    for (int j = i - 1; j >= 0; j--) {
        if (binaryNum[j] == 1)
            c = c | 1u << j;
    }
}

// ============================================================================

class FsFile {
    int file_size;
    int block_in_use;
    int index_block;
    int block_size;
    int free_space_in_last_block = -1;

    public:
        FsFile(int _block_size) {
            file_size = 0;
            block_in_use = 0;
            block_size = _block_size;
            index_block = -1;
        }
        int getIndexBlock() {
            return this->index_block;
        }
        void setIndexBlock(int block_num) {
            this->index_block = block_num;
        }
        int getfile_size(){
            return file_size;
        }
        void setFileSize(int fileSize) {
            this->file_size = fileSize;
        }
        int getBlocksInUse() {
            return this->block_in_use;
        }
        void setBlocksInUse(int blocks) {
            this->block_in_use = blocks;
        }
        int getFreeSpaceInLastBlock() {
            return this->free_space_in_last_block;
        }
        void setFreeSpaceInLastBlock(int free) {
            this->free_space_in_last_block = free;
        }
};

// ============================================================================

class FileDescriptor {
    string file_name;
    FsFile* fs_file;
    bool inUse;

    public:
    FileDescriptor(string FileName, FsFile *fsi) {
        file_name = FileName;
        fs_file = fsi;
        inUse = true;
    }

    string getFileName() {
        return file_name;
    }

    FsFile *getFile() {
        return this->fs_file;
    }

    int isInUse() {
        return this->inUse;
    }

    void setUse(bool isUse) {
        inUse = isUse;
    }
};

// class to represent main dir
class myMainDir {
    map<string, FileDescriptor *> dir;
    public:
        // overriding the [] operator to access the hashmap easily
        FileDescriptor* operator[](string fileName) {
            if (dir.find(fileName) != dir.end()) {
                return dir[fileName];
            }
            return NULL;
        }

        // contains functions to avoid having a getter function to dir for safer code
        // and to avoid using find != end all the time
        bool contains(string fileName) {
            return dir.find(fileName) != dir.end();
        }

        // overriding the begin and end methods making our class iterable
        map<string, FileDescriptor *>::iterator begin() {
            return dir.begin();
        }
        map<string, FileDescriptor *>::iterator end() {
            return dir.end();
        }

        //return the FsFile related with the filename or NULL if no such file
        FsFile* getFile(string fileName) {
            if (dir.find(fileName) != dir.end()) {
                return dir[fileName]->getFile();
            }
            return NULL;
        }

        void deleteFile(string fileName) {
            dir.erase(fileName);
        }

        void addFile(string fileName, FileDescriptor *file) {
            dir[fileName] = file;
        }
};

#define DISK_SIM_FILE "DISK_SIM_FILE.txt"

// ============================================================================

class fsDisk {
    FILE *sim_disk_fd;
    bool is_formated;
    // BitVector - "bit" (int) vector, indicate which block in the disk is free
	//              or not.  (i.e. if BitVector[0] == 1 , means that the
	//             first block is occupied.
    int BitVectorSize;
    int *BitVector;
    // (5) MainDir --
    myMainDir maindir;
    // Structure that links the file name to its FsFile
    // (6) OpenFileDescriptors --
    std::vector<FileDescriptor *> OpenFileDescriptors;
    //  when you open a file,
    // the operating system creates an entry to represent that file
    // This entry number is the file descriptor.
    int blockSize = -1;
    int spaceInUse = 0; // total space in use
    map<string, vector<int>> indexBlocksMap; // vector of filename and his index block's indexes
                                            // eg file 'A' index block [1, 2, 3, 4] 
    vector<FileDescriptor *> allFiles; // only for print function, since hashmap ain't sorted and
                                       // our files have to be indexed by order of creation
    public:

    // function to find empty block
    int findEmptyBlock() {
        for (int i = 0; i < BitVectorSize; i++) {
            if (BitVector[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    // check whether a file is open or not. 1=open, 0=closed
    int isOpen(string fileName) {
        if (maindir[fileName] != NULL && maindir[fileName]->isInUse() == true) {
            return 1;
        }
        return 0;
    }

    // ------------------------------------------------------------------------
    fsDisk() {
        sim_disk_fd = fopen(DISK_SIM_FILE , "r+");
        assert(sim_disk_fd);
        for (int i=0; i < DISK_SIZE ; i++) {
            int ret_val = fseek ( sim_disk_fd , i , SEEK_SET );
            ret_val = fwrite( "\0" ,  1 , 1, sim_disk_fd);
            assert(ret_val == 1);
        }
        fflush(sim_disk_fd);
        is_formated = false;
    }
	
    // ------------------------------------------------------------------------
    void listAll() {
        int i = 0;
        for (i = 0; i < allFiles.size(); i++) {
            cout << "index: " << i << ": FileName: " << allFiles[i]->getFileName() << " , isInUse: " << allFiles[i]->isInUse() << endl;    
        }    
        char bufy;
        cout << "Disk content: '";
        for (i = 0; i < DISK_SIZE; i++) {
            cout << "(";
            int ret_val = fseek(sim_disk_fd, i, SEEK_SET);
            ret_val = fread(&bufy, 1, 1, sim_disk_fd);
            cout << bufy;
            cout << ")";
        }
        cout << "'" << endl;
    }


 
    // ------------------------------------------------------------------------
    void fsFormat( int blockSize = 4 ) {
        if (DISK_SIZE % blockSize != 0) {
            cout << "please use block size that divise disk size" << endl;
            return;
        }
        this->BitVectorSize = DISK_SIZE / blockSize;
        BitVector = (int*)malloc(sizeof(int)*BitVectorSize);
        this->blockSize = blockSize;
        this->is_formated = true;
        cout << "FORMAT DISK: number of blocks: " << BitVectorSize << endl;
    }

    // ------------------------------------------------------------------------
    int CreateFile(string fileName) {
        if (!is_formated) {
            // cout << "DEBUG: Format the disk(2) before doing anything else" << endl;
            return -1;
        }
        // if file doesn't exist, create it and open it for usage
        if (maindir[fileName] == NULL) {
            FsFile *newfile = new FsFile(this->blockSize);
            FileDescriptor *newfd = new FileDescriptor(fileName, newfile);
            maindir.addFile(fileName, newfd);
            allFiles.push_back(newfd);
            indexBlocksMap[fileName] = vector<int>();
            // look for free spot resulted from file deletion
            for (int i = 0; i < OpenFileDescriptors.size() ;i++) {
                if (OpenFileDescriptors[i] == NULL) {
                    OpenFileDescriptors[i] = newfd;
                    return i;
                }
            }
            // else add to the back
            OpenFileDescriptors.push_back(newfd);
            return OpenFileDescriptors.size() - 1;
        } 
        return -1;
    }

    // ------------------------------------------------------------------------
    int OpenFile(string fileName) {
        // if file doesn't exist
        if (maindir[fileName] == NULL) return -1;
        // if file is already open
        if (isOpen(fileName) == 1) return -1;
        FileDescriptor *fd = maindir[fileName];
        // find the fd in the openfds array
        for (int i = 0; i < OpenFileDescriptors.size(); i++) {
            if (OpenFileDescriptors[i]->getFileName() == fd->getFileName()) {
                OpenFileDescriptors[i]->setUse(true);
                return i;
            }
        }
        return -1;
    }  

    // ------------------------------------------------------------------------
    string CloseFile(int fd) {
        if (OpenFileDescriptors.size() > fd) {
            OpenFileDescriptors[fd]->setUse(false);
            return OpenFileDescriptors[fd]->getFileName();
        }
        return "-1";
    }
    // ------------------------------------------------------------------------
    int WriteToFile(int fd, char *buf, int len ) {
        // file not found
        if ((OpenFileDescriptors.size() <= fd) || OpenFileDescriptors[fd] == NULL) {
            // cout << "DEBUG: File Descriptor not found" << endl;
            return -1;
        }
        // file is closed
        if (OpenFileDescriptors[fd]->isInUse() == false) {
            // cout << "DEBUG: File is closed" << endl;
            return -1;
        }
        // sys format
        if (!is_formated) {
            // cout << "DEBUG: Format the disk(2) before doing anything else" << endl;
            return -1;
        }
        FsFile *file = OpenFileDescriptors[fd]->getFile();
        // file has no space
        if ((blockSize * blockSize) - file->getfile_size() < len) {
            // cout << "DEBUG: Not enough space in file to write" << endl;
            return -1;
        }
        // disk has no space
        if (spaceInUse + len > DISK_SIZE) {
            // cout << "DEBUG: Disk has no space to write" << endl;
            return -1;
        }
        int blocksRequired;
        // new (empty) file -> need len/blocksize blocks + 1 for index block
        if (file->getIndexBlock() == -1) {
            blocksRequired = ceil((float)len / (float)blockSize) + 1;
        } else { // not new file -> no + 1 // also check if theres an half empty block to fill
            blocksRequired = ceil(((float)len - (float)file->getFreeSpaceInLastBlock()) / (float)blockSize);
        }
        int availableBlocks = 0;
        for (int i = 0; i < BitVectorSize; i++) {
            if (BitVector[i] == 0) {
                availableBlocks++;
            }
        }
        if (availableBlocks < blocksRequired) {
            // cout << "DEBUG: Not enough free blocks to write" << endl;
            return -1;
        }
        int bufIndex = 0; // buffer runner
        string fileName = OpenFileDescriptors[fd]->getFileName();
        if (file->getIndexBlock() == -1) { // first block will be index block, rest are data blocks
            int indexBlock = findEmptyBlock();
            BitVector[indexBlock] = 1; // mark index block as occupied
            file->setIndexBlock(indexBlock);
            int i = 1; // blocks runner
            while (i < blocksRequired) {
                int currentDataBlock = findEmptyBlock();
                indexBlocksMap[fileName].push_back(currentDataBlock); // add data block index to map of index blocks vectors
                fseek(sim_disk_fd, (indexBlock*blockSize) + i - 1, SEEK_SET); // seek the index block 
                fprintf(sim_disk_fd, "%c", currentDataBlock + '0'); // write the index of the data block into the index block
                // writeToBlock(dataBlock, indexBlock);
                BitVector[currentDataBlock] = 1; // mark data block as occupied
                fseek(sim_disk_fd, currentDataBlock * blockSize, SEEK_SET); // seek current data block to write to
                int inBlockRunner = 0; // indicating when the block is full and we need to jump to next block
                while (bufIndex < len && inBlockRunner < blockSize) { // write data to data block
                    fwrite(&buf[bufIndex], 1, 1, sim_disk_fd);
                    inBlockRunner++;
                    bufIndex++;
                }
                i++;
            }
            file->setFileSize(len); // update file fields
            file->setBlocksInUse(blocksRequired - 1);
            file->setFreeSpaceInLastBlock(blockSize - (len % blockSize));
        } else { // all 0,...,requiredblocks are data blocks
            // begin by filling the fillable block from a previous write
            if (file->getFreeSpaceInLastBlock() > 0) {
                fseek(sim_disk_fd, (indexBlocksMap[fileName].back() * blockSize) + (blockSize - file->getFreeSpaceInLastBlock()), SEEK_SET); // seek the fillable block and move inside it to the empty space
                while (bufIndex < file->getFreeSpaceInLastBlock() && bufIndex < len) {
                    fwrite(&buf[bufIndex], 1 , 1, sim_disk_fd);
                    bufIndex++;
                }
            }
            int i = 0; // block runner
            while (i < blocksRequired) {
                int currentDataBlock = findEmptyBlock();
                indexBlocksMap[fileName].push_back(currentDataBlock); // add data block index to map of index blocks vectors
                BitVector[currentDataBlock] = 1; // mark index block as occupied
                fseek(sim_disk_fd, (file->getIndexBlock() * blockSize) + file->getBlocksInUse() + i, SEEK_SET); // seek the next open spot in our index block
                fprintf(sim_disk_fd, "%c", currentDataBlock + '0'); // write the index of the data block into the index block
                fseek(sim_disk_fd, currentDataBlock * blockSize, SEEK_SET); // seek current data block to write to
                int inBlockRunner = 0;
                while (bufIndex < len && inBlockRunner < blockSize) { // write data to data block
                    fwrite(&buf[bufIndex], 1, 1, sim_disk_fd);
                    inBlockRunner++;
                    bufIndex++;
                }
                i++;
            }
            file->setBlocksInUse(file->getBlocksInUse() + blocksRequired);
            file->setFileSize(file->getfile_size() + len);
            if (len - file->getFreeSpaceInLastBlock() <= 0) {
                file->setFreeSpaceInLastBlock(file->getFreeSpaceInLastBlock() - len);
            } else {
                file->setFreeSpaceInLastBlock(blockSize - ((len - file->getFreeSpaceInLastBlock()) % blockSize));
            }
        }
        this->spaceInUse += len; 
        return len;
    }
    // ------------------------------------------------------------------------
    int DelFile( string FileName ) {
        if (!is_formated) {
            // cout << "DEBUG: Format the disk(2) before doing anything else" << endl;
            return -1;
        }
        if (maindir[FileName] == NULL) {
            // cout << "DEBUG: file not found, nothing to delete" << endl;
            return -1;
        }
        int fd = -1;
        FsFile *file = maindir.getFile(FileName);
        for (int i = 0; i < allFiles.size(); i++) {
            if (allFiles[i]->getFileName() == FileName) {
                allFiles.erase(allFiles.begin() + i);
                break;
            }
        }
        spaceInUse -= file->getfile_size();
        for (int i = 0; i < OpenFileDescriptors.size(); i++) { // delete from openfds
                if (OpenFileDescriptors[i] != NULL && OpenFileDescriptors[i]->getFileName() == FileName) {
                    OpenFileDescriptors[i] = NULL;
                    fd = i;
                    break;
                }
            }
        BitVector[file->getIndexBlock()] = 0; // delete index block from bitvector
        fseek(sim_disk_fd, file->getIndexBlock() * blockSize, SEEK_SET);
        for (int i = 0; i < blockSize; i++) {
            fwrite("\0", 1, 1, sim_disk_fd); // overwrite nulls on indexes inside index block
        }
        for (int i = 0; i < indexBlocksMap[FileName].size(); i++) { 
            int dataBlockIndex = indexBlocksMap[FileName][i]; // delete data blocks from bit vector
            fseek(sim_disk_fd, dataBlockIndex * blockSize, SEEK_SET); // seek data block to overwrite with nulls
            for (int j = 0; j < blockSize; j++) {
                fwrite("\0", 1, 1, sim_disk_fd); // overwrite nulls on file bytes
            }
            BitVector[dataBlockIndex] = 0;
        }
        indexBlocksMap.erase(FileName);
        maindir.deleteFile(FileName);
        return fd;
    }
    // ------------------------------------------------------------------------
    int ReadFromFile(int fd, char *buf, int len ) {
        if (!is_formated) {
            // cout << "DEBUG: Format the disk(2) before doing anything else" << endl;
            return -1;
        }
        if ((OpenFileDescriptors.size() <= fd) || OpenFileDescriptors[fd] == NULL) {
            // cout << "DEBUG: File Descriptor not found" << endl;
            return -1;
        }
        if (OpenFileDescriptors[fd]->isInUse() == false) {
            // cout << "DEBUG: File is closed" << endl;
            return -1;
        }
        string fileName = OpenFileDescriptors[fd]->getFileName();
        FsFile *file = maindir.getFile(fileName);
        for (int i = 0; i < indexBlocksMap[fileName].size(); i++) {
            int dataBlock = indexBlocksMap[fileName][i];
            fseek(sim_disk_fd, dataBlock * blockSize, SEEK_SET);
            fread(&buf[i*blockSize], sizeof(char), blockSize, sim_disk_fd);
        }
        buf[len] = '\0';
        return 0;
    }
};
    
int main() {
    int blockSize; 
	int direct_entries;
    string fileName;
    char str_to_write[DISK_SIZE];
    char str_to_read[DISK_SIZE];
    int size_to_read; 
    int _fd;

    fsDisk *fs = new fsDisk();
    int cmd_;
    while(1) {
        cin >> cmd_;
        switch (cmd_)
        {
            case 0:   // exit
				delete fs;
				exit(0);
                break;

            case 1:  // list-file
                fs->listAll(); 
                break;
          
            case 2:    // format
                cin >> blockSize;
                fs->fsFormat(blockSize);
                break;
          
            case 3:    // creat-file
                cin >> fileName;
                _fd = fs->CreateFile(fileName);
                cout << "CreateFile: " << fileName << " with File Descriptor #: " << _fd << endl;
                break;
            
            case 4:  // open-file
                cin >> fileName;
                _fd = fs->OpenFile(fileName);
                cout << "OpenFile: " << fileName << " with File Descriptor #: " << _fd << endl;
                break;
             
            case 5:  // close-file
                cin >> _fd;
                fileName = fs->CloseFile(_fd); 
                cout << "CloseFile: " << fileName << " with File Descriptor #: " << _fd << endl;
                break;
           
            case 6:   // write-file
                cin >> _fd;
                cin >> str_to_write;
                fs->WriteToFile( _fd , str_to_write , strlen(str_to_write) );
                break;
          
            case 7:    // read-file
                cin >> _fd;
                cin >> size_to_read ;
                fs->ReadFromFile( _fd , str_to_read , size_to_read );
                cout << "ReadFromFile: " << str_to_read << endl;
                break;
           
            case 8:   // delete file 
                 cin >> fileName;
                _fd = fs->DelFile(fileName);
                cout << "DeletedFile: " << fileName << " with File Descriptor #: " << _fd << endl;
                break;
            default:
                break;
        }
    }

} 