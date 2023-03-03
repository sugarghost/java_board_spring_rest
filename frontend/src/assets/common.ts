// Description: 공통으로 사용되는 함수들을 정의한다.

// 날짜를 포멧팅해서 반환하기 위한 함수
const formatDate = (date: string) => {
  if (!date) {
    return "-";
  }
  const formatedDate = new Date(date);

  const options = <Intl.DateTimeFormatOptions>{
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    hour12: false,
  };
  return formatedDate.toLocaleDateString("ko-KR", options);
};

// 날짜를 포멧팅해서 반환하기 위한 함수
// 초까지 포함
// 기존에 formatDate에 option 파라미터를 추가해서 나중에 합치는 방안을 고려
const formatDateSecond = (date: string) => {
  if (!date) {
    return "-";
  }
  const formatedDate = new Date(date);

  const options = <Intl.DateTimeFormatOptions>{
    year: "numeric",
    month: "2-digit",
    day: "2-digit",
    hour: "2-digit",
    minute: "2-digit",
    second: "2-digit",
    hour12: false,
  };
  return formatedDate.toLocaleDateString("ko-KR", options);
};

// 입력된 length를 기준으로 문자열을 자르고 마지막에 ...을 붙여서 반환
// 문자가 입력된 length보다 작으면 그냥 리턴
const subStringWithSkipMark = (str: string, length: number) => {
  const skipMark = "…";
  if (str.length > length) {
    return str.substring(0, length) + skipMark;
  }
  return str;
};
export { formatDate, formatDateSecond, subStringWithSkipMark };
