import "./Dashboard.css";
import { FiUsers } from "react-icons/fi";
import { HiOutlineShoppingBag } from "react-icons/hi";
import { FiShoppingCart } from "react-icons/fi";
import { PiMoneyLight } from "react-icons/pi";
import SimpleBarChart from "./BarChart";
import { getCategoriesStatistic } from "../../../services/dashboardService";
import { useState, useEffect } from "react";
import { convertPrice } from "../../../utils/utils";

export default function AdminDashboard() {
  const [statistics, setStatistics] = useState({
    countCustomer: 0,
    countProduct: 0,
    countOrder: 0,
    earning: 0,
  });

  useEffect(() => {
    const fetchStatistics = async () => {
      try {
        const res = await getCategoriesStatistic();
        console.log(res.data);
        setStatistics(res.data);
      } catch (error) {
        console.error("Error fetching data:", error);
      }
    };

    fetchStatistics();
  }, []);
  return (
    <div className="admin-dashboard" id="admin-dashboard">
      <div className="dashboard-header">
        <div
          className="dashboard-item"
          style={{ borderLeft: "solid 4px rgb(2, 158, 2)" }}
        >
          <div className="dashboard-item-content">
            <div
              className="dashboard-item-content-icon"
              style={{ backgroundColor: "rgb(2, 158, 2)" }}
            >
              <FiUsers />
            </div>
            <div className="dashboard-item-content-info">
              <h3>{statistics?.countCustomer}</h3>
              <div>Customer</div>
            </div>
          </div>
        </div>
        <div
          className="dashboard-item"
          style={{ borderLeft: "solid 4px #ffc107" }}
        >
          <div className="dashboard-item-content">
            <div
              className="dashboard-item-content-icon"
              style={{ backgroundColor: "#ffc107" }}
            >
              <HiOutlineShoppingBag />
            </div>
            <div className="dashboard-item-content-info">
              <h3>{statistics?.countOrder}</h3>
              <div>Orders</div>
            </div>
          </div>
        </div>
        <div
          className="dashboard-item"
          style={{ borderLeft: "solid 4px #dc3545" }}
        >
          <div className="dashboard-item-content">
            <div
              className="dashboard-item-content-icon"
              style={{ backgroundColor: "#dc3545" }}
            >
              <FiShoppingCart />
            </div>
            <div className="dashboard-item-content-info">
              <h3>{statistics?.countProduct}</h3>
              <div>Products</div>
            </div>
          </div>
        </div>
        <div
          className="dashboard-item"
          style={{ borderLeft: "solid 4px #0dcaf0" }}
        >
          <div className="dashboard-item-content">
            <div
              className="dashboard-item-content-icon"
              style={{ backgroundColor: "#0dcaf0" }}
            >
              <PiMoneyLight />
            </div>
            <div className="dashboard-item-content-info">
              <h3>{convertPrice(statistics?.earning)}</h3>
              <div>Earnings</div>
            </div>
          </div>
        </div>
      </div>
      <div className="simple-bar-chart">
        <SimpleBarChart />
      </div>
    </div>
  );
}
