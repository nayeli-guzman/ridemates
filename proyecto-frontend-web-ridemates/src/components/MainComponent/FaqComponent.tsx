import { useState } from "react";

const faqs = [
  {
    question: "¿Qué es RideMates?",
    answer:
      "RideMates es una aplicación de carpooling que ayuda a los estudiantes universitarios a llegar a sus destinos.",
  },
  {
    question: "¿Cómo comienzo a utilizar RideMates?",
    answer:
      "Para empezar, regístrate en nuestra plataforma para empezar a encontrar viajes disponibles en tu zona.",
  },
  {
    question: "¿RideMates es seguro?",
    answer:
      "Sí, priorizamos la seguridad de nuestros usuarios al asegurar las cuentas de los usuarios y la verificación de los conductores.",
  },
];

export default function FAQ() {
  const [openIndex, setOpenIndex] = useState<number>(-1);

  const toggleFAQ = (index: number) => {
    setOpenIndex(openIndex === index ? -1 : index);
  };

  return (
    <div className="max-w-3xl mx-auto p-6">
      <h2 className="text-3xl font-semibold text-center mb-8 space-y-4">
        ¿Tienes alguna pregunta?
      </h2>
      <div className="space-y-5">
        {faqs.map((faq, index) => (
          <div
            key={index}
            className="bg-white rounded-lg p-4 shadow-md duration-300 ease-in-out transition-all transform hover:scale-105"
          >
            <button
              className="flex justify-between items-center w-full text-left text-lg font-semibold text-purple-700 focus:outline-none"
              onClick={() => toggleFAQ(index)}
            >
              {faq.question}
              <span className="ml-4">
                {openIndex === index ? (
                  <svg
                    className="w-5 h-5 transform rotate-180"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M19 9l-7 7-7-7"
                    />
                  </svg>
                ) : (
                  <svg
                    className="w-5 h-5"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M5 15l7-7 7 7"
                    />
                  </svg>
                )}
              </span>
            </button>
            {openIndex === index && (
              <p className="mt-5 text-black">{faq.answer}</p>
            )}
          </div>
        ))}
      </div>
    </div>
  );
}
