import { axiosClient } from "../../App";

export default async function getRoute(routeId: string) {
  return await axiosClient.get(`/route/${routeId}`);
}
