import { jwtDecode, JwtPayload } from "jwt-decode";
interface DecodedToken extends JwtPayload {
  role: string;
}
export function getRoleBasedOnToken() {
  const token = localStorage.getItem("token");
  if (!token) {
    throw new Error("No token found");
  }
  try {
    const decodedToken = jwtDecode(token) as DecodedToken;
    return decodedToken.role;
  } catch (error) {
    throw new Error("Invalid token format");
  }
}
