"use client";
import React, { useEffect, useState } from "react";
import { Sidebar, SidebarBody, SidebarLink } from "./sidebar";
import {
  MagnifyingGlassIcon,
  StarIcon,
  PlusCircleIcon,
  CreditCardIcon,
  UserMinusIcon,
  StrikethroughIcon,
  CogIcon,
} from "@heroicons/react/24/solid";
import { Link } from "react-router-dom";
import { motion } from "framer-motion";
// import Image from "next/image";
import { cn } from "../../lib/utils";
import perfilImg from '../../assets/perfil.png';
import { ChatBubbleBottomCenterTextIcon, MinusIcon } from "@heroicons/react/24/outline";
import RideMatesTitle from "../GeneralComponent/RideMatesComponent";
import { UserResponse } from "../../dtos/User/UserResponse";
import { getUserMe } from "../../services/UserService/GetUser";

export function SidebarDemo({
  children,
}: Readonly<{ children: React.ReactNode }>) {

  const [user, setUser] = useState<UserResponse>({
    firstName: "-",
    lastName: "-",
    email: "-",
    role: "-",
    calification: 0,
    createdAt: "-",
    birthDate: "-",
  });

  const fetchUser = async () => {
    const response = await getUserMe();
    setUser(response.data);
  };

  useEffect(() => {
    fetchUser();
  }, []);

  const links = [
    {
      label: "Encontrar Rutas",
      href: "/available-routes",
      icon: <MagnifyingGlassIcon className="h-6 w-6" />,
    },
    {
      label: "Publicar Ruta",
      href: "/add-route",
      icon: <PlusCircleIcon className="h-6 w-6" />,
    },
    {
      label: "Historial de Pagos",
      href: "/payments",
      icon: <CreditCardIcon className="h-6 w-6" />,
    },
  ];
  const [open, setOpen] = useState(false);

  return (
    <div
      className={cn(
        "rounded-md flex flex-col md:flex-row bg-gray-100 dark:bg-neutral-800 w-full flex-1 max-w-full mx-auto border border-neutral-200 dark:border-neutral-700 overflow-auto",
        "h-screen", // for your use case, use `h-screen` instead of `h-[60vh]`
      )}
    >
      <Sidebar open={open} setOpen={setOpen}>
        <SidebarBody className="justify-between gap-10">
          <div className="flex flex-col flex-1 overflow-y-auto overflow-x-hidden">
            {open ? <Logo /> : <LogoIcon />}
            <div className="mt-8 flex flex-col gap-2">
              {links.map((link, idx) => (
                <SidebarLink key={idx} link={link} />
              ))}
            </div>
          </div>
          <div>

          <SidebarLink link={{
                  label: "Cerrar sesión",
                  href: "/logout",
                  icon: <MinusIcon className="h-6 w-6" />,
            }} />
            <SidebarLink
              link={{
                label: `${user.firstName} ${user.lastName}`,
                href: "/my-info",

                icon: (
                  <img
                    src={perfilImg}
                    className="h-7 w-7 flex-shrink-0 rounded-full"
                    width={50}
                    height={50}
                    alt="Avatar"
                  />
                ),
              }}
            />
          </div>
        </SidebarBody>
      </Sidebar>
      {children}
    </div>
  );
}
export const Logo = () => {
  return (
    <Link
      to="#"
      className="font-normal flex space-x-2 items-center text-sm text-black py-1 relative z-20"
    >
      <div>
        <RideMatesTitle className="hover:text-black" />
      </div>
      <motion.span
        initial={{ opacity: 0 }}
        animate={{ opacity: 1 }}
        className="font-medium text-black dark:text-white whitespace-pre"
      ></motion.span>
    </Link>
  );
};
export const LogoIcon = () => {
  return (
    <Link
      to="#"
      className="font-normal flex space-x-2 items-center text-sm text-black py-1 relative z-20"
    >
      <div className="flex-shrink-0">
        <img
          src="https://docs.material-tailwind.com/img/logo-ct-dark.png"
          alt="brand"
          className="h-7 w-7"
        />
      </div>
    </Link>
  );
};
