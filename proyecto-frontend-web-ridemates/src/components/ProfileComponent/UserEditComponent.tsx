import { useState } from "react";

// @material-tailwind/react
import {
  Input,
  Typography,
  Select,
  Option,
  Popover,
  PopoverHandler,
  PopoverContent,
  Card,
  Button,
  IconButton,
  Alert,
  AlertProps,
} from "@material-tailwind/react";

// day picker
import { format } from "date-fns";
import { DayPicker } from "react-day-picker";

// @heroicons/react
import { ArrowLeftIcon } from "@heroicons/react/24/solid";
import { useNavigate } from "react-router-dom";

import { patchUser } from "../../services/UserService/PatchUser";
import { UserPatchDto } from "../../dtos/User/UserPatchDto";

export default function UserEditComponent() {
  const [date, setDate] = useState();
  const [showAlert, setShowAlert] = useState(false);
  const [alertMessage, setAlertMessage] = useState("");
  const [alertColor, setAlertColor] = useState<AlertProps["color"] | undefined>(
    undefined,
  );
  const [data, setData] = useState<UserPatchDto>({});

  const [confirmEmail, setConfirmEmail] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const navigate = useNavigate();

  const validateEmail = (email: string) => {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
  };

  const handleSubmit = async () => {
    // I hate you Javascript
    if (
      data.email === "" ||
      (data.email === undefined && data.firstName === "") ||
      (data.firstName === undefined && data.lastName === "") ||
      (data.lastName === undefined && data.gender === "") ||
      (data.gender === undefined && data.password === "") ||
      (data.password === undefined && data.phone === "") ||
      data.phone === undefined
    ) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("No hay cambios por enviar!");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    if (data.email !== confirmEmail) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("Los correos electrónicos no coinciden.");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    if (!validateEmail(data.email)) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("Formato de correo electrónico no válido.");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    if (data.password !== confirmPassword) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("Las contraseñas no coinciden.");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    if (data.password.length < 8) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("La contraseña debe tener al menos 8 carácteres.");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    try {
      await patchUser(data);
    } catch (error) {
      setShowAlert(true);
      setAlertColor("gray" as AlertProps["color"]);
      setAlertMessage("Error al actualizar la información.");
      setTimeout(() => {
        setShowAlert(false);
      }, 5000); // 5 segundos en milliseconds
      return;
    }

    setShowAlert(true);
    setAlertColor("green" as AlertProps["color"]);
    setAlertMessage("Información actualizada correctamente.");
    setTimeout(() => {
      setShowAlert(false);
    }, 5000); // 5 segundos en milliseconds
  };

  return (
    <>
      <div className="relative container mx-auto px-8 py-20">
        {showAlert && (
          <Alert
            color={alertColor}
            className="absolute top-4 right-4 w-full max-w-md"
          >
            {alertMessage}
          </Alert>
        )}
        <Card className="px-8 py-6 container mx-auto">
          <IconButton
            className="mb-6 bg-transparent shadow-none"
            onClick={() => navigate(-1)}
          >
            <ArrowLeftIcon className="h-6 w-6 text-gray-700" />
          </IconButton>
          <Typography variant="h5" color="blue-gray">
            Editar información de cuenta
          </Typography>
          <Typography
            variant="small"
            className="text-gray-600 font-normal mt-1"
          >
            Actualiza tu información personal y perfil.
          </Typography>
          <div className="flex flex-col mt-8">
            <div className="mb-6 flex flex-col items-end gap-4 md:flex-row">
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Nombres
                </Typography>
                <Input
                  size="lg"
                  placeholder="Emma"
                  value={data.firstName || ""}
                  onChange={(e) =>
                    setData({ ...data, firstName: e.target.value })
                  }
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Apellidos
                </Typography>
                <Input
                  size="lg"
                  placeholder="Roberts"
                  value={data.lastName || ""}
                  onChange={(e) =>
                    setData({ ...data, lastName: e.target.value })
                  }
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
            </div>
            <div className="mb-6 flex flex-col gap-4 md:flex-row">
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className=" font-medium"
                >
                  Género
                </Typography>
                <Select
                  size="lg"
                  value={data.gender || ""}
                  onChange={(e) => setData({ ...data, gender: e })}
                  className="h-10 border-t-blue-gray-200 aria-[expanded=true]:border-t-primary"
                >
                  <Option>Hombre</Option>
                  <Option>Mujer</Option>
                </Select>
              </div>
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Fecha de nacimiento
                </Typography>
                <Popover placement="bottom">
                  <PopoverHandler>
                    <Input
                      size="lg"
                      onChange={(e) =>
                        setData({ ...data, birthDate: e.target.value })
                      }
                      placeholder="Selecciona una fecha"
                      value={date ? format(date, "yyyy/MM/dd") : ""}
                      labelProps={{
                        className: "hidden",
                      }}
                      className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                    />
                  </PopoverHandler>
                  <PopoverContent>
                    <DayPicker
                      mode="single"
                      selected={date}
                      onSelect={setDate as any}
                      showOutsideDays
                      className="border-0"
                      classNames={{
                        caption:
                          "flex justify-center py-2 mb-4 relative items-center",
                        caption_label: "text-sm !font-medium text-gray-900",
                        nav: "flex items-center",
                        nav_button:
                          "h-6 w-6 bg-transparent hover:bg-blue-gray-50 p-1 rounded-md transition-colors duration-300",
                        nav_button_previous: "absolute left-1.5",
                        nav_button_next: "absolute right-1.5",
                        table: "w-full border-collapse",
                        head_row: "flex !font-medium text-gray-900",
                        head_cell: "m-0.5 w-9 !font-normal text-sm",
                        row: "flex w-full mt-2",
                        cell: "text-gray-600 rounded-md h-9 w-9 text-center text-sm p-0 m-0.5 relative [&:has([aria-selected].day-range-end)]:rounded-r-md [&:has([aria-selected].day-outside)]:bg-gray-900/20 [&:has([aria-selected].day-outside)]:text-white [&:has([aria-selected])]:bg-gray-900/50 first:[&:has([aria-selected])]:rounded-l-md last:[&:has([aria-selected])]:rounded-r-md focus-within:relative focus-within:z-20",
                        day: "h-9 w-9 p-0 !font-normal",
                        day_range_end: "day-range-end",
                        day_selected:
                          "rounded-md bg-gray-900 text-white hover:bg-gray-900 hover:text-white focus:bg-gray-900 focus:text-white",
                        day_today: "rounded-md bg-gray-200 text-gray-900",
                        day_outside:
                          "day-outside text-gray-500 opacity-50 aria-selected:bg-gray-500 aria-selected:text-gray-900 aria-selected:bg-opacity-10",
                        day_disabled: "text-gray-500 opacity-50",
                        day_hidden: "invisible",
                      }}
                      /*
                  components={{
                    IconLeft: ({ ...props }) => (
                      <ChevronLeftIcon
                        {...props}
                        className="h-4 w-4 stroke-2"
                      />
                    ),
                    IconRight: ({ ...props }) => (
                      <ChevronRightIcon
                        {...props}
                        className="h-4 w-4 stroke-2"
                      />
                    ),
                  }}*/
                    />
                  </PopoverContent>
                </Popover>
              </div>
            </div>
            <div className="mb-6 flex flex-col items-end gap-4 md:flex-row">
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Correo electrónico
                </Typography>
                <Input
                  size="lg"
                  placeholder="emma@mail.com"
                  onChange={(e) => setData({ ...data, email: e.target.value })}
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Confirmar correo electrónico
                </Typography>
                <Input
                  size="lg"
                  onChange={(e) => setConfirmEmail(e.target.value)}
                  placeholder="emma@mail.com"
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
            </div>
            <div className="mb-6 flex flex-col items-end gap-4 md:flex-row">
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Contraseña
                </Typography>
                <Input
                  size="lg"
                  onChange={(e) =>
                    setData({ ...data, password: e.target.value })
                  }
                  placeholder="Password"
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  Confirmar contraseña
                </Typography>
                <Input
                  size="lg"
                  onChange={(e) => setConfirmPassword(e.target.value)}
                  placeholder="Password"
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
            </div>
            <div className="mb-6 flex flex-col w-[440px] items-end gap-4 md:flex-row">
              <div className="w-full">
                <Typography
                  variant="small"
                  color="blue-gray"
                  className="mb-2 font-medium"
                >
                  N. Teléfono
                </Typography>
                <Input
                  size="lg"
                  placeholder="+123 0123 456 789"
                  labelProps={{
                    className: "hidden",
                  }}
                  className="w-full placeholder:opacity-100 focus:border-t-primary border-t-blue-gray-200"
                />
              </div>
            </div>
            <div className="mb-6 flex flex-col items-end gap-4 md:flex-row">
              <div className="w-full">
                <Button size="sm" onClick={() => handleSubmit()}>
                  Actualizar
                </Button>
              </div>
            </div>
          </div>
        </Card>
      </div>
    </>
  );
}
