export interface IComment {
  commentId: bigint;
  articleId: bigint;
  content: string;
  createdDate: Date;
}
