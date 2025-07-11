import { jwtDecode } from "jwt-decode";

export function isTokenExpired(token: string) {
  if (!token) return true;
  try {
    const decodedToken = jwtDecode(token);
    if (!decodedToken.exp) return true;

    const currentTime = Date.now() / 1000;
    console.log("token expired: ", decodedToken.exp < currentTime);
    return decodedToken.exp < currentTime;
  } catch (error) {
    console.error("Error decoding token:", error);
    return true;
  }
}

export function getUsername(token: any) {
  if (!token) return null;
  try {
    const decodedToken = jwtDecode(token);
    return decodedToken.sub || null;
  } catch (error) {
    console.error("Error decoding token:", error);
    return null;
  }
}
