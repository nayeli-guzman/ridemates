interface RideMatesTitleProps {
  className?: string;
}

export default function RideMatesTitle({
  className = "",
}: RideMatesTitleProps) {
  return (
    <a
      className={`btn btn-ghost style-navbar-link text-transparent bg-clip-text font-inter font-bold ${className}`}
    >
      RideMates
    </a>
  );
}
