import { axiosClient } from "../../App";
import { UserPatchDto } from "../../dtos/User/UserPatchDto";

export async function patchUser(user: UserPatchDto) {
  return await axiosClient.patch("/user/me", user);
}
