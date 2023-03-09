export interface IArticle {
  articleId: bigint;
  categoryId: bigint;
  categoryName: string;
  writer: string;
  content: string;
  title: string;
  viewCount: number;
  createdDate: string;
  modifiedDate: string;
  isFileExist: boolean;
}
export interface ISearchParams {
  categoryId: bigint;
  startDate: Date;
  endDate: Date;
  searchWord: string;
  articlePerPage: number;
  pageNum: number;
}
