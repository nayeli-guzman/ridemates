import {
  Card,
  CardBody,
  Typography,
  Avatar,
  IconButton,
} from "@material-tailwind/react";
import image from "../../assets/proof.jpg";
import { useEffect, useState } from "react";
import { PencilIcon } from "@heroicons/react/24/solid";
import { useNavigate } from "react-router-dom";
import ReviewComponent from "../ReviewComponent/ReviewComponent";
import { UserResponse } from "../../dtos/User/UserResponse";
import { getUser, getUserMe } from "../../services/UserService/GetUser";

interface ProfileCardProps {
  search: boolean;
  self: boolean;
}

export default function ProfileCard({ self, search }: ProfileCardProps) {
  const [showEdit, setShowEdit] = useState<boolean>(false);
  const [userId, setUserId] = useState<number>(-1);
  const [loading, setLoading] = useState<boolean>(false);
  const [user, setUser] = useState<UserResponse>({
    firstName: "-",
    lastName: "-",
    email: "-",
    role: "-",
    calification: 0,
    createdAt: "-",
    birthDate: "-",
  });
  const navigate = useNavigate();

  const fetchUser = async () => {
    const fun = self ? getUserMe() : getUser(userId);
    const response = await fun;
    setUser(response.data);
    setLoading(false);
  };

  if (search) {
    const queryParams = new URLSearchParams(location.search);
    setUserId(Number(queryParams.get("user")));
  }

  useEffect(() => {
    setLoading(true);
    fetchUser();
  }, []);

  const calculateAge = (birthdate: string) => {
    const birthDate = new Date(birthdate);
    const today = new Date();
    let age = today.getFullYear() - birthDate.getFullYear();
    const monthDifference = today.getMonth() - birthDate.getMonth();
    if (
      monthDifference < 0 ||
      (monthDifference === 0 && today.getDate() < birthDate.getDate())
    ) {
      age--;
    }
    return age;
  };

  if (loading) {
    return (
      <div className="flex flex-col w-full items-center">
        <Card className="p-4 w-full max-w-md shadow-lg">
          <CardBody className="flex flex-col items-center">
            <Typography
              variant="h6"
              color="blue-gray"
              className="text-center mb-2"
            >
              Cargando...
            </Typography>
          </CardBody>
        </Card>
      </div>
    );
  }

  return (
    <div className="flex flex-col w-full items-center">
      <Card className="p-4 w-full max-w-md shadow-lg">
        <CardBody className="flex flex-col items-center">
          <div
            className="flex items-center text-center mb-4"
            onMouseEnter={() => setShowEdit(true)}
            onMouseLeave={() => setShowEdit(false)}
          >
            <Avatar
              src={image}
              alt={user.firstName}
              size="lg"
              className="w-24 border-4 border-gray-300 rounded-full"
            />
            {showEdit && (
              <IconButton
                className="ml-2 bg-gray-300 rounded-full flex items-center text-center justify-center"
                onClick={() => navigate("/edit-info")}
              >
                <PencilIcon className="h-5 w-5 text-gray-500" />
              </IconButton>
            )}
          </div>
          <div className="mt-2">
            <Typography
              variant="h5"
              color="blue-gray"
              className="text-center mb-2"
            >
              {user.firstName} {user.lastName},{" "}
              <span className="normal-text">
                {calculateAge(user.birthDate)}
              </span>
            </Typography>
            <Typography variant="h6" color="gray" className="text-center">
              {user.role == "DRIVER" ? "Conductor" : "Pasajero"}
            </Typography>
          </div>
          <hr className="w-full border-t border-gray-300 my-4" />
          <div className="flex justify-around w-full mt-4">
            <div className="text-center mb-2">
              <Typography variant="h6" color="blue" className="bold-text">
                {user.calification}
              </Typography>
              <Typography variant="h6" color="gray" className="smaller-text">
                Calificación
              </Typography>
            </div>
            <div className=" border-l border-gray-300 h-12 mx-4"></div>
            <div className="text-center mb-2">
              <Typography variant="h6" color="blue" className="bold-text">
                {user.createdAt}
              </Typography>
              <Typography variant="h6" color="gray" className="smaller-text">
                Desde
              </Typography>
            </div>
          </div>
        </CardBody>
      </Card>
      <div className="w-full max-w-md">
        <ReviewComponent />
      </div>
    </div>
  );
}
