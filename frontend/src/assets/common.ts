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
export { formatDate, formatDateSecond };
