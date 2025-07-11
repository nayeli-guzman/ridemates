import { Card, IconButton } from "@material-tailwind/react";
import {
  CardBody,
  Typography,
  CardFooter,
  Button,
} from "@material-tailwind/react";
import { Page } from "../../models/SpringPage";
import { PaymentResponseDto } from "../../dtos/Payment/PaymentResponseDto";
import { useEffect, useState } from "react";
import { Status } from "../../models/PaymentStatus";
import { getMyPayments } from "../../services/PaymentsService/GetMyPayments";

export default function TravelComponent() {
  const [page, setPage] = useState<number>(1);
  const [payments, setPayments] = useState<Page<PaymentResponseDto>>();

  const handlePayments = async () => {
    // const response = await getMyPayments(page, 10);
    setPayments({
      content: [{
        travelId: 1,
        amount: 5,
        createdAt: new Date().toLocaleDateString(),
        status: "ACCEPTED",
        method: "VISA",
        paymentIntentId: "pi_3QT2vNK0rNehPlAI0TzhnfVq",
      }]
    });
  }

  useEffect(() => {
    handlePayments();
  }, [page]);

  return (
    <>
      <Card className="style-card-payments overflow-x-hidden">
        <CardBody>
          <Typography variant="h5" color="black" className="mb-2">
            Historial de pagos
          </Typography>
          <table className="w-full text-left table-auto min-w-max">
            <thead>
              <tr>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                    Transacción
                  </p>
                </th>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                    Cantidad
                  </p>
                </th>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                    Fecha
                  </p>
                </th>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                    Estado
                  </p>
                </th>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70">
                    Método de pago
                  </p>
                </th>
                <th className="p-4 border-y border-blue-gray-100 bg-blue-gray-50/50">
                  <p className="block font-sans text-sm antialiased font-normal leading-none text-blue-gray-900 opacity-70"></p>
                </th>
              </tr>
            </thead>
            {payments?.content.map((payment, _) => (
              <tbody>
                <tr>
                  <td className="p-4 border-b border-blue-gray-50">
                    <div className="flex items-center gap-3">
                      <p className="block font-sans text-sm antialiased font-bold leading-normal text-blue-gray-900">
                        {payment.paymentIntentId}
                      </p>
                    </div>
                  </td>
                  <td className="p-4 border-b border-blue-gray-50">
                    <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                      ${payment.amount}
                    </p>
                  </td>
                  <td className="p-4 border-b border-blue-gray-50">
                    <p className="block font-sans text-sm antialiased font-normal leading-normal text-blue-gray-900">
                      {payment.createdAt}
                    </p>
                  </td>
                  <td className="p-4 border-b border-blue-gray-50">
                    <div className="w-max">
                      <div
                        className={`relative grid items-center px-2 py-1 font-sans text-xs font-bold uppercase rounded-md select-none whitespace-nowrap text-green-900 bg-green-500/20`}
                      >
                        <span className="">{"PAGADO"}</span>
                      </div>
                    </div>
                  </td>
                  <td className="p-4 border-b border-blue-gray-50">
                    <div className="flex items-center gap-3">
                      <div className="w-12 p-1 border rounded-md h-9 border-blue-gray-50">
                        <img
                          src={`https://demos.creative-tim.com/test/corporate-ui-dashboard/assets/img/logos/${payment.method.toLowerCase()}.png`}
                          alt="visa"
                          className="relative inline-block h-full w-full !rounded-none  object-contain object-center p-1"
                        />
                      </div>
                      <div className="flex flex-col">
                        <p className="block font-sans text-sm antialiased font-normal leading-normal capitalize text-blue-gray-900">
                          {payment.method}
                        </p>
                      </div>
                    </div>
                  </td>
                </tr>
              </tbody>
            ))}
          </table>
        </CardBody>
        <CardFooter className="flex items-center justify-between border-t border-blue-gray-50 p-4">
          <Button variant="outlined" size="sm">
            Atrás
          </Button>
          <div className="flex items-center justify-center gap-2">
            {Array.from(Array(payments?.totalPages || 1).keys()).map((_, i) => (
              <IconButton
                key={i}
                onClick={() => setPage(i)}
                size="sm"
                color="black"
                className="items-center"
              >
              {i + 1} 
              </IconButton>
            ))}
          </div>
          <Button variant="outlined" size="sm">
            Siguiente
          </Button>
        </CardFooter>
      </Card>
    </>
  );
}
