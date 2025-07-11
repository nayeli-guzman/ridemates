import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import RideMatesTitle from "./RideMatesComponent";
import "../../index.css";

interface NavBarProps {
  aboutRef: React.RefObject<HTMLDivElement>;
  faqRef: React.RefObject<HTMLDivElement>;
}

export default function NavBar(props: NavBarProps) {
  const [isScrolled, setIsScrolled] = useState(false);
  const navigate = useNavigate();

  const scrollTo = (ref: React.RefObject<HTMLDivElement>) => {
    if (ref.current) {
      ref.current.scrollIntoView({ behavior: "smooth" });
    }
  };

  useEffect(() => {
    const handleScroll = () => {
      if (window.scrollY > 150) {
        // Cambia el valor según lo necesites
        setIsScrolled(true);
      } else {
        setIsScrolled(false);
      }
    };

    window.addEventListener("scroll", handleScroll);

    return () => {
      window.removeEventListener("scroll", handleScroll);
    };
  }, []);
  return (
    <div
      className={`navbar bg-base-100 fixed top-0 transition-all duration-700 ${isScrolled ? "bg-white backdrop-blur-lg shadow-lg" : "bg-white/30"}`}
      style={{
        backgroundColor: isScrolled ? "rgba(0, 0, 0, 0.8)" : "transparent",
      }}
    >
      <div className="navbar-start mx-20">
        <RideMatesTitle />
      </div>

      <div className={`navbar-end hidden mx-36 lg:flex`}>
        <ul
          className={`menu menu-horizontal px-1 ${isScrolled ? "text-white" : "text-black"}`}
        >
          <li>
            <a
              className={
                "btn btn-ghost font-poppins duration-300 ease-in-out transform hover:scale-105 transition-all hover:text-white"
              }
              onClick={() => scrollTo(props.faqRef)}
            >
              FAQ
            </a>
          </li>
          <li>
            <a
              className={
                "btn btn-ghost font-poppins duration-300 ease-in-out transform hover:scale-105 transition-all hover:text-white"
              }
              onClick={() => scrollTo(props.aboutRef)}
            >
              About
            </a>
          </li>
          <li>
            <a
              className={
                "btn btn-ghost font-poppins duration-300 ease-in-out transform hover:scale-105 transition-all hover:text-white"
              }
              onClick={() => navigate("/auth/register")}
            >
              Sign Up
            </a>
          </li>
          <li>
            <a
              className={`btn btn-ghost font-poppins duration-300 ease-in-out transform hover:scale-105 transition-all hover:text-white`}
              onClick={() => navigate("/auth/login")}
            >
              Log In
            </a>
          </li>
        </ul>
        <div className="dropdown text-white">
          <div tabIndex={0} role="button" className="btn btn-ghost">
            <svg
              xmlns="http://www.w3.org/2000/svg"
              className="h-5 w-5  duration-300 ease-in-out transform hover:scale-110 transition-all"
              fill=""
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth="2"
                d="M4 6h16M4 12h8m-8 6h16"
              />
            </svg>
          </div>
          <ul
            tabIndex={0}
            className="menu menu-sm dropdown-content bg-base-100 rounded-box z-[1] mt-3 w-52 p-2 shadow text-black "
          >
            <li>
              <a>Help</a>
            </li>
            <li>
              <a>About</a>
            </li>
            <li>
              <a>O</a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  );
}

/*
  return (
    <div 
      className={`h-40 px-10 py-5 flex justify-between items-center sticky top-0  z-10 transition-all duration-300`} 
      style={{ backgroundColor: isScrolled ? '#627D5E' : 'transparent' }}  // Color personalizado en línea
    >
      <div className={` px-24 text-lg font-semibold text-2xl ${isScrolled ? 'text-white' : 'text-black'}`}>Ride-Mates</div>

      <div className="flex-1 flex justify-center">
        <button
          id="register"
          onClick={() => navigate("/auth/register")}
          className={`text-lg font-semibold ${isScrolled ? 'text-white' : 'text-black'}`}
        >
          Únete
        </button>
      </div>
      <div className="w-16"></div>
    </div>
  );
}

*/
