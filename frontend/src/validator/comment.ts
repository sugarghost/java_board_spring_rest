export default function commentRules() {
  const commentContentRules = [
    (value: string) => !!value || "내용을 입력해주세요",
    (value: string) => /^.{1,255}$/g.test(value) || "내용은 1~255자로 입력해주세요",
  ];

  return {
    commentContentRules,
  };
}
