import { axiosClient } from "../../App";
import { RouteRequestDto } from "../../dtos/Route/RouteRequestDto";

export async function postRoute(route: RouteRequestDto) {
  console.log("Post Route Request:");
  console.log(route);

  const response = await axiosClient.post("/route/driver", route);

  console.log("Post Route Response:");
  console.log(response.data);

  return response.data;
}
