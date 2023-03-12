export interface IFile {
  fileId: bigint;
  articleId: bigint;
  fileOriginName: string;
  fileSaveName: string;
  fileType: string;
  filePath: string;
  createdDate: Date;
}
