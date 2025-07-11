import { useState, useRef, useCallback } from "react";
import useRides from "../../hooks/useRoutes";
import Route from "./Route";
import { RouteResponseDto } from "../../dtos/Route/RouteResponseDto";

export default function AvailableRoutesList() {
  const [page, setPage] = useState(0);
  const {
    isLoading,
    isError,
    //  error,
    results,
    hasNextPage,
  } = useRides(page);

  const [selectedRoute, setSelectedRoute] = useState<RouteResponseDto | null>(
    null,
  );
  const intObserver = useRef<IntersectionObserver | null>(null);
  const lastRouteRef = useCallback(
    (route: HTMLElement | null) => {
      if (isLoading) return;

      if (intObserver.current) intObserver.current.disconnect();

      intObserver.current = new IntersectionObserver((routes) => {
        if (routes[0].isIntersecting && hasNextPage) {
          console.log("We are near the last route");
          setPage((prev) => prev + 1);
        }
      });

      if (route) intObserver.current.observe(route);
    },
    [isLoading, hasNextPage],
  );

  if (isError)
    return (
      <p className="flex justify-center items-center text-1xl font-bold text-gray-800 my-8">
        Cargando...
      </p>
    );

  const handleClick = (route: RouteResponseDto) => {
    setSelectedRoute(route);
  };

  const content = results.map((route, i) => {
    //console.log(route)
    if (results.length === i + 1) {
      return (
        <div key={route.id} >
          <Route ref={lastRouteRef} key={route.id} route={route} />
        </div>
      );
    }
    return <Route key={route.id} route={route} />;
  });

  return (
    <>
      <h1
        id="top"
        className="flex justify-center items-center text-1xl font-bold text-gray-800 my-8"
      >
        Rutas Recientes
      </h1>
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-3 gap-10 mr-10 ml-10">
          {content}
      </div>
      {isLoading && (
        <p className="flex justify-center items-center text-1xl font-bold text-gray-800 my-8">
          {" "}
          Loading more
        </p>
      )}
    </>
  );
}
