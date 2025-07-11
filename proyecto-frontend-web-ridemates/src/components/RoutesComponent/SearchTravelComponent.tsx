import { useNavigate } from "react-router-dom";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";
import SearchResults from "./SearchResultComponent";
import { useEffect, useState } from "react";
import { axiosClient } from "../../App";

interface SearchTravelProps {
  className?: string;
}

export default function SearchTravel(props: SearchTravelProps) {
  const [query, setQuery] = useState<string>("");
  const [results, setResults] = useState<RouteResponseDto[]>([]);
  const navigate = useNavigate();

  const handleClick = async (route: RouteResponseDto) => {
    navigate(`/booking?route=${route.id}`);
    console.log("to booking");
  };

  const fetchRoutes = async () => {
    const response = await axiosClient.get("/route/all?page=0&size=10");
    console.log(response.data.content);
    setResults([...results, ...response.data.content]);
  };

  useEffect(() => {
    if (!query) {
      setResults([]);
      return;
    }

    const timeoutId = setTimeout(() => {
      const data = [
        {
          id: 1,
          origin: "Barcelona",
          destination: "Madrid",
          capacity: 4,
          dateTime: "2023-10-01",
          driverFullName: "Salvador Dalí",
        },
        {
          id: 2,
          origin: "Madrid",
          destination: "Barcelona",
          capacity: 3,
          dateTime: "2023-10-01",
          driverFullName: "Pablo Picasso",
        },
      ];

      const filteredResults = data.filter((item) =>
        item.origin.toLowerCase().includes(query.toLowerCase()),
      );
      setResults(filteredResults);

      fetchRoutes();
    }, 500); // Delay in milliseconds

    return () => clearTimeout(timeoutId);
  }, [query]);

  return (
    <>
      <div className={`max-w-[500px]  ${props.className}`}>
        <div className="relative">
          <input
            className="text-black w-full h-12 placeholder:text-slate-400  text-sm border border-slate-200 rounded-md pl-3 pr-28 py-2 transition duration-300 ease focus:outline-none focus:border-slate-400 hover:border-slate-300 "
            placeholder="Buscar viaje..."
            onChange={(e) => setQuery(e.target.value)}
          />
          <button
            className="absolute top-1 right-1 flex h-10 items-center rounded bg-purple-600 py-1 px-2.5 border border-transparent text-center text-sm text-black transition-all shadow-sm hover:shadow focus:bg-slate-700 focus:shadow-none active:bg-slate-700 hover:bg-slate-700 active:shadow-none disabled:pointer-events-none disabled:opacity-50 disabled:shadow-none"
            type="button"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              viewBox="0 0 24 24"
              fill="currentColor"
              className="w-4 h-4 mr-2"
            >
              <path
                fillRule="evenodd"
                d="M10.5 3.75a6.75 6.75 0 1 0 0 13.5 6.75 6.75 0 0 0 0-13.5ZM2.25 10.5a8.25 8.25 0 1 1 14.59 5.28l4.69 4.69a.75.75 0 1 1-1.06 1.06l-4.69-4.69A8.25 8.25 0 0 1 2.25 10.5Z"
                clipRule="evenodd"
              />
            </svg>
            Buscar
          </button>
          {query && (
            <div className="absolute top-full w-full left-0 bg-gray-50 border border-slate-200 rounded-2xl mt-1 z-10">
              <SearchResults
                results={results}
                className="justify-center w-full"
                handleClick={handleClick}
              />
            </div>
          )}
        </div>
      </div>
    </>
  );
}
