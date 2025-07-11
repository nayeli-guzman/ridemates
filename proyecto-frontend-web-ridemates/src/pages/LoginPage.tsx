import { useState } from "react";
import LoginForm from "../components/AuthComponent/LoginForm";

export default function LoginPage() {
  const [data, setData] = useState({ email: "", password: "" });

  return (
    <>
      <div className="h-screen w-full flex">
        <div className="w-1/2 h-full flex items-center justify-center">
          <LoginForm data={data} setData={setData}></LoginForm>
        </div>

        <div className="w-1/2 h-full main-gradient-bg"></div>
      </div>
    </>
  );
}
