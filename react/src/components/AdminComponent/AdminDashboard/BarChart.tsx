import React, { useState, useEffect } from "react";
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
import { getCategoriesReport } from "../../../services/dashboardService";

interface CategorySoldReportDTO {
  categoryName: string;
  totalSold: number;
}

interface GenderData {
  name: string;
  categorySoldReportDTOS: CategorySoldReportDTO[];
}

interface ProductData {
  message: string;
  success: boolean;
  data: GenderData[];
}

export default function SimpleBarChart() {
  const [productData, setProductData] = useState<GenderData[] | null>(null);

  const getNumberOfSold = async () => {
    try {
      const { data } = await getCategoriesReport();
      console.log(data);
      const products: GenderData[] = data;
      console.log(products);
      setProductData(products);
    } catch (error) {
      console.error("Error fetching data:", error);
    }
  };

  console.log(productData);

  useEffect(() => {
    getNumberOfSold();
  }, []);

  const transformDataForChart = () => {
    const transformedData: any[] = [];

    if (productData) {
      console.log(productData);
      productData.forEach((genderData) => {
        if (genderData.categorySoldReportDTOS) {
          genderData.categorySoldReportDTOS.forEach((category) => {
            const existingCategory = transformedData.find(
              (item) => item.categoryName === category.categoryName
            );

            if (existingCategory) {
              existingCategory[genderData.name] = category.totalSold;
            } else {
              const newCategory = {
                categoryName: category.categoryName,
                [genderData.name]: category.totalSold,
              };
              transformedData.push(newCategory);
            }
          });
        }
      });
    }
    console.log(transformedData);
    return transformedData;
  };

  const chartData = transformDataForChart();
  console.log(chartData);

  return (
    <BarChart
      width={1100}
      height={500}
      data={chartData}
      margin={{ top: 5, right: 30, left: 20, bottom: 5 }}
    >
      <CartesianGrid strokeDasharray="3 3" />
      <XAxis dataKey="categoryName" />
      <YAxis />
      <Tooltip />
      <Legend />
      <Bar dataKey="Nam" fill="#8884d8" />
      <Bar dataKey="Nữ" fill="#82ca9d" />
    </BarChart>
  );
}
