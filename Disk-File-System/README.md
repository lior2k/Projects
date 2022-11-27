# Disk File System

Design and implement a disk file system (DFS). The disk file system handles a single disk at any given time, it simulates the way file names, file locations and file data are saved and arranged over a hard disk. The job of the file system is to map all the file fragments that are stored on the disk. The fragments of the files are saved in small sections called 'blocks' and are organized into a single logical unit. The parts of the file are not saved directly and continously on the disk but actually scattered over it. Our DFS will simulate a file system with a small disk and a single directory. 

- The disk will actually be a file.
- One directory.
- Indexed Allocation

## Objectives

| Plugin | README |
| ------ | ------ |
| FsFormat | Format the disk blocks to given size |
| CreateFile | Create a new file and open it for usage |
| OpenFile | Open a closed file |
| CloseFile | Close an opened file |
| DeleteFile | Delete file according to given file name |
| WriteToFile | Write to file according to given file descriptor and string to write |
| ReadFromFile | Read from file according to given file descriptor and size to read |
| listAll | Show disk contents |

## Sample Output

- Format disk to blocks of 4 bytes, create 'FileOne', write 'HelloWorld' into it and print disk contents.

[![N|Solid](https://i.ibb.co/Wnf8bsT/Pic1.png)

- Create 'FileTwo', write 'HelloFileTwo' into it and print disk contents.

[![N|Solid](https://i.ibb.co/j35hRt3/Pic2.png)

- Write 'FROMF1' to FileOne and print disk contents.

[![N|Solid](https://i.ibb.co/h2wnQCV/Pic3.png)

- Read 16 bytes from file with file descriptor 0 i.e FileOne, then 12 bytes from FileTwo.

[![N|Solid](https://i.ibb.co/hythcPs/Pic4.png)

- Close FileOne, Delete FileTwo and print disk contents.

[![N|Solid](https://i.ibb.co/vsqpbYw/Pic5.png)


















