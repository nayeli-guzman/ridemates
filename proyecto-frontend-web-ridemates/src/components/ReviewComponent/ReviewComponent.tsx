import { Card, CardBody, Typography, Avatar } from "@material-tailwind/react";
import { format } from "date-fns";
import { es } from "date-fns/locale";
import { useState } from "react";

export default function ReviewComponent() {
  const [page, setPage] = useState(1);
  const [reviews, setReviews] = useState([
    {
      id: 1,
      name: "John Doe",
      date: "2023-10-01",
      avatar: "https://via.placeholder.com/150",
      review:
        "This is a great product! I really enjoyed using it and would recommend it to others.",
    },
    {
      id: 2,
      name: "Jane Smith",
      date: "2023-09-28",
      avatar: "https://via.placeholder.com/150",
      review:
        "Not bad, but there are some improvements needed. Overall, a decent experience.",
    },
    {
      id: 3,
      name: "Alice Johnson",
      date: "2023-09-25",
      avatar: "https://via.placeholder.com/150",
      review:
        "I had a fantastic experience! The customer service was excellent and the product exceeded my expectations.",
    },
  ]);

  return (
    <div className="container mx-auto px-4 py-8">
      <Typography variant="h4" color="blue-gray" className="mb-6 text-center">
        Reviews
      </Typography>
      <div className="flex flex-col space-y-4">
        {reviews.map((review) => (
          <Card key={review.id} className="w-full max-w-2xl mx-auto shadow-lg">
            <CardBody>
              <div className="flex items-center mb-4">
                <Avatar
                  src={review.avatar}
                  alt={review.name}
                  size="sm"
                  className="rounded-full mr-4"
                />
                <div>
                  <Typography variant="h5" color="blue-gray">
                    {review.name}
                  </Typography>
                  <Typography variant="small" color="gray">
                    {format(new Date(review.date), "PPPP", { locale: es })}
                  </Typography>
                </div>
              </div>
              <Typography variant="small" color="gray" className="break-words">
                {review.review}
              </Typography>
            </CardBody>
          </Card>
        ))}
      </div>
    </div>
  );
}
