import NavBar from "../components/GeneralComponent/NavBar";
import { useRef } from "react";
import AboutComponent from "../components/MainComponent/AboutComponent";
import FaqComponent from "../components/MainComponent/FaqComponent";
import { JoinUsComponent } from "../components/MainComponent/JoinUsComponent";
import phoneImage from "../assets/phone.jpeg";

export default function MainPage() {
  const aboutRef = useRef<HTMLDivElement>(null);
  const faqRef = useRef<HTMLDivElement>(null);

  return (
    <>
      <div className="w-full h-full main-gradient-bg">
        <NavBar aboutRef={aboutRef} faqRef={aboutRef} />
        <div className="flex flex-col items-center justify-center w-full h-full text-center overflow-hidden main-app-container">
          <div className="flex flex-row w-full justify-center items-center">
            <div className="flex-none w-1/2 space-y-3">
              <h1 className="text-4xl font-bold text-white">
                ¡Bienvenido a RideMates!
              </h1>
              <p className="text-white">
                Viaja seguro y barato a tus clases universitarias
              </p>
            </div>
            <div className="flex-none mt-52 w-1/2 h-1/2 flex justify-center">
              <div className="mockup-phone w-1/2 h-1/2 object-cover mt-80">
                <div className="camera "></div>
                <div className="display">
                  <img src={phoneImage} alt="Phone Display" className="" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="text-center md:p-24">
        <div className="w-full flex flex-col justify-center items-center" ref={aboutRef}>
          <AboutComponent />
        </div>
        <div ref={faqRef}>
          <FaqComponent />
        </div>
        <JoinUsComponent />
      </div>
    </>
  );
}
