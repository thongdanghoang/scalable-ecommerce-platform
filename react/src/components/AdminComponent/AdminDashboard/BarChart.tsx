import {
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
} from "recharts";
import "./BarChart.css";
import { useState, useEffect } from "react";
import { getAllClothes } from "../../../services/clothesService";

// const data = [
//   {
//     name: "Page A",
//     uv: 4000,
//     pv: 2400,
//     amt: 2400,
//   },
//   {
//     name: "Page B",
//     uv: 3000,
//     pv: 1398,
//     amt: 2210,
//   },
//   {
//     name: "Page C",
//     uv: 2000,
//     pv: 9800,
//     amt: 2290,
//   },
//   {
//     name: "Page D",
//     uv: 2780,
//     pv: 3908,
//     amt: 2000,
//   },
//   {
//     name: "Page E",
//     uv: 1890,
//     pv: 4800,
//     amt: 2181,
//   },
//   {
//     name: "Page F",
//     uv: 2390,
//     pv: 3800,
//     amt: 2500,
//   },
//   {
//     name: "Page G",
//     uv: 3490,
//     pv: 4300,
//     amt: 2100,
//   },
// ];

export default function SimpleBarChart() {
  const [numberOfSold, setNumberOfSold] = useState<
    { name: string; sold: number }[]
  >([]);

  const getNumberOfSold = async () => {
    try {
      const res = await getAllClothes();
      const { products } = await res?.json();

      if (products) {
        const categories: string[] = [
          "ÁO SƠ MI",
          "QUẦN ÂU",
          "ÁO THUN",
          "QUẦN SHORT",
          "ÁO KHOÁC",
          "ÁO POLO",
          "QUẦN JEAN",
          "CHÂN VÁY",
          "ÁO CHỐNG NẮNG",
        ];
        const data: { name: string; sold: number }[] = [];

        categories.forEach((category) => {
          const types = products.filter((item: any) =>
            item.category.includes(category)
          );

          const sumOfSold = types.reduce(
            (acc: any, item: any) => acc + (item.numberOfSold || 0),
            0
          );

          data.push({ name: category, sold: sumOfSold });
        });

        setNumberOfSold(data);
      } else {
        console.error("Dữ liệu không hợp lệ hoặc không chứa numberOfSold");
      }
    } catch (error) {
      console.error("Đã xảy ra lỗi: ", error);
    }
  };

  useEffect(() => {
    getNumberOfSold();
  }, []);

  return (
    <BarChart
      width={1100}
      height={500}
      data={numberOfSold}
      margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="name" />
      <YAxis />
      <Tooltip />
      <Legend />
      <Bar dataKey="sold" fill="#8884d8" />
    </BarChart>
  );
}
