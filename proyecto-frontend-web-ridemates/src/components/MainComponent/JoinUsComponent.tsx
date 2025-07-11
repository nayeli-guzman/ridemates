import { useNavigate } from "react-router-dom";

export function JoinUsComponent() {
  const navigate = useNavigate();
  return (
    <>
      <h3 className="text-2xl font-semibold mb-4 mt-10">
        ¿Qué esperas para unirte a nosotros?
      </h3>
      <div className="space-x-10">
        <button
          className="btn glass bg-purple-600 shadow-md duration-300 ease-in-out transform hover:scale-110 text-white px-6 py-2 rounded-lg hover:bg-purple-400 transition-all"
          onClick={() => navigate("/auth/register")}
        >
          Sign in
        </button>
        <button
          className="btn glass bg-gray-600 shadow-md duration-300 ease-in-out transform hover:scale-110 transition-all text-white px-6 py-2 rounded-lg hover:bg-gray-700 "
          onClick={() => navigate("/auth/login")}
        >
          Log in
        </button>
      </div>
    </>
  );
}
