import { axiosClient } from "../../App";

export async function getAllRoutes(page: number) {
	const response = await axiosClient.get(`/route/all?page=${page}&size=10`);
	console.log("Get All Routes Response");
	console.log(response.data.content);
	console.log("ajksnjkasnjknaskjasnjk");
	return response.data;
}
