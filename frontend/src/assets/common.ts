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

const subStringWithSkipMark = (str: string, length: number) => {
  const skipMark = "…";
  if (str.length > length) {
    return str.substring(0, length) + skipMark;
  }
  return str;
};
export { formatDate, formatDateSecond, subStringWithSkipMark };
