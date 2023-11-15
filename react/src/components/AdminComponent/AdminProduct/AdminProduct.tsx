import { CloseOutlined, EditOutlined } from "@ant-design/icons";
import {
  Upload,
  Button,
  Card,
  Form,
  Input,
  Space,
  Typography,
  Select,
  Drawer,
  Row,
  Col,
} from "antd";
import { useState, useEffect } from "react";
import "./AdminProduct.css";
import { BiPlus } from "react-icons/bi";
import TableComponent from "../../TableComponent/TableComponent";
import { useMutation, useQuery } from "@tanstack/react-query";
import {
  createNewClothes,
  getAllClothes,
  getCategories,
  getClothesById,
  updateClothes,
  uploadImageClothes,
} from "../../../services/clothesService";
import ReactQuill from "react-quill";
import "react-quill/dist/quill.snow.css";
import { UploadOutlined } from "@ant-design/icons";
import { toast } from "react-toastify";
import { convertPrice, toastMSGObject } from "../../../utils/utils";
import { Action } from "../../../model/ActionModal";
import { API_URL } from "../../../utils/constants";

export default function AdminProduct() {
  const [form] = Form.useForm();
  const [isOpenDrawer, setisOpenDrawer] = useState(false);
  const [rowSelected, setRowSelected] = useState<any>({});
  const [value, setValue] = useState("");
  const [listCate, setListCate] = useState<any[]>([]);
  const [typeAction, setTypeAction] = useState<Action>(Action.ADD);

  useEffect(() => {
    form.setFieldsValue({
      classifyClothes: [
        {
          quantities: [1],
        },
      ],
    });
  }, []);

  const handleOnOpenDrawer = (action: Action) => {
    setisOpenDrawer(true);
    setTypeAction(action);
  };

  const handleOnCloseDrawer = () => {
    setisOpenDrawer(false);
    form.resetFields();
    form.setFieldsValue({
      classifyClothes: [
        {
          quantities: [1], // reset lại form quantities trong classifyClothes thành 1 cái
        },
      ],
    });
  };

  const newClothesCustome = () => {
    const formClothes = form.getFieldsValue();
    const classifyClothesCustome =
      formClothes.classifyClothes &&
      formClothes.classifyClothes.map((item: any) => ({
        ...item,
        images: item?.images?.map((file: any) => file?.name),
      }));
    return { ...formClothes, classifyClothes: classifyClothesCustome };
  };

  // Get all clothes via API

  const fetchGetAllProducts = async () => {
    const res = await getAllClothes();
    const { products, totalCount } = await res?.json();
    return { products, totalCount };
  };

  const queryAllProducts = useQuery(["all-product"], fetchGetAllProducts);
  const { data: listProducts, isLoading: isLoadingProducts } = queryAllProducts;
  console.log(listProducts);

  // get detail clothes via API

  const fetchGetProductById = async (context: any) => {
    const idClothes = context?.queryKey[1];
    const res = await getClothesById(idClothes);
    return res;
  };

  const queryProductDetail = useQuery(
    ["product-detail-1", rowSelected.id],
    fetchGetProductById,
    { enabled: !!rowSelected.id }
  );
  const { data: productDetail, isSuccess: isSuccessProduct } = queryProductDetail

  useEffect(() => {
    if (isSuccessProduct && typeAction === Action.UPDATE && isOpenDrawer) {
      console.log(typeAction);
      form.setFieldsValue({
        ...productDetail,
        discount: +productDetail.discount * 100,
        classifyClothes: productDetail.classifyClothes.map((classify: any) => ({
          ...classify,
          images: classify.images.map((img: string) => ({
            uid: -1,
            name: img,
            url: `${API_URL}/api/products/images/${img}`,
          })),
        })),
      });
    }
  }, [isSuccessProduct, isOpenDrawer]);

  // get all categories

  useEffect(() => {
    getCategories().then((res) => setListCate(res));
  }, []);

  // add new clothes

  const mutationAddClo = useMutation(
    async (data: any) => {
      const res = await createNewClothes(data);
      return res.data;
    },
    {
      onSuccess: () => {
        toast.success("Thêm sản phẩm mới thành công", toastMSGObject());
        handleOnCloseDrawer();
      },
      onError: () => {
        toast.error("Thêm sản phẩm mới thất bại", toastMSGObject());
      },
      onSettled: () => {
        queryAllProducts.refetch();
      },
    }
  );

  const handleAddNewClothes = () => {
    mutationAddClo.mutate({
      ...newClothesCustome(),
      discount: newClothesCustome().discount / 100,
    });
  };

  // update clothes

  const mutationUpdateClo = useMutation(
    async (data: any) => {
      const res = await updateClothes(data);
      return res.data;
    },
    {
      onSuccess: () => {
        toast.success(
          "Cập nhật thông tin sản phẩm thành công",
          toastMSGObject()
        );
        handleOnCloseDrawer();
      },
      onError: () => {
        toast.error("Cập nhật thông tin sản phẩm thất bại", toastMSGObject());
      },
      onSettled: () => {
        queryAllProducts.refetch();
        queryProductDetail.refetch();
      },
    }
  );

  const handleUpdateClothes = () => {
    mutationUpdateClo.mutate({
      id: productDetail.id,
      ...newClothesCustome(),
      discount: newClothesCustome().discount / 100,
    });
  };

  const renderAction = () => {
    return (
      <div>
        <EditOutlined
          style={{ color: "orange", fontSize: "30px", cursor: "pointer" }}
          onClick={() => handleOnOpenDrawer(Action.UPDATE)}
        />
      </div>
    );
  };

  const columns = [
    {
      title: "Tên chi tiết",
      dataIndex: "name",
      render: (text: string) => <span>{text}</span>,
      width: 400,
      sorter: (a: any, b: any) => a.name.length - b.name.length,
      isSearchProps: true,
    },
    {
      title: "Đơn vị lưu kho",
      dataIndex: "sku",
      render: (text: string) => <span>{text}</span>,
      sorter: (a: any, b: any) => a.sku.length - b.sku.length,
      isSearchProps: true,
    },
    {
      title: "Giảm giá",
      dataIndex: "discount",
      render: (text: number) => <span>{text * 100} %</span>,
      sorter: (a: any, b: any) => a.discount - b.discount,
    },
    {
      title: "Giá tiền",
      dataIndex: "price",
      render: (text: number) => <span>{convertPrice(text)}</span>,
      sorter: (a: any, b: any) => a.price - b.price,
      filters: [
        {
          text: "Dưới 50k",
          value: [0, 50000],
        },
        {
          text: "Từ 50k đến 200k",
          value: [50000, 200000],
        },
        {
          text: "Từ 200k đến 500k",
          value: [200000, 500000],
        },
        {
          text: "Từ 500k đến 1000k",
          value: [500000, 1000000],
        },
        {
          text: "Trên 1000k",
          value: [1000000],
        },
      ],
      onFilter: ([start, end]: number[], record: any) =>
        end
          ? record.price <= end && record.price >= start
          : record.price >= start,
    },
    {
      title: "Danh mục",
      dataIndex: "category",
    },
    {
      title: "Action",
      dataIndex: "action",
      render: renderAction,
    },
  ];

  return (
    <div id="AdminProduct">
      <div className="clo-act-btn">
        <div className="total-clo">
          Tổng số lượng sản phẩm : {listProducts?.totalCount}
        </div>
        <Button type="primary" onClick={() => handleOnOpenDrawer(Action.ADD)}>
          <BiPlus />
          Add new clothes
        </Button>
      </div>

      <TableComponent
        columns={columns}
        listData={listProducts?.products}
        isLoading={isLoadingProducts}
        onRow={(record: any, rowIndex: any) => {
          return {
            onClick: (event: any) => {
              setRowSelected(record);
            },
          };
        }}
        isRowSelection={false}
      />

      {/** form add clothes */}
      <Drawer
        title={
          typeAction === Action.ADD
            ? "Tạo quần áo mới"
            : "Cập nhật thông tin quần áo"
        }
        width={1200}
        onClose={handleOnCloseDrawer}
        open={isOpenDrawer}
        bodyStyle={{ paddingBottom: "80px" }}
        extra={
          <Space>
            <Button onClick={handleOnCloseDrawer}>Cancel</Button>
            <Button
              onClick={
                typeAction === Action.ADD
                  ? handleAddNewClothes
                  : handleUpdateClothes
              }
              type="primary"
            >
              {typeAction.toLocaleLowerCase()}
            </Button>
          </Space>
        }
      >
        <Form
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          form={form}
          autoComplete="off"
          initialValues={{ classifyClothes: [{}] }}
        >
          <Row gutter={32}>
            <Col span={14} style={{ borderRight: "1px solid #e1e1e1" }}>
              <Form.Item
                name="name"
                label="Tên chi tiết"
                rules={[
                  { required: true, message: "Vui lòng nhập tên quần áo" },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="sku"
                label="Đơn vị lưu kho"
                rules={[
                  { required: true, message: "Vui lòng nhập đơn vị lưu kho" },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="price"
                label="Giá tiền"
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập giá tiền",
                  },
                  {
                    validator: (_, value) =>
                      value == null ||
                      (value && value >= 1000 && value % 100 === 0)
                        ? Promise.resolve()
                        : Promise.reject(
                            "Giá tiền phải lớn hơn hoặc bằng 1000 và đúng định dạng như : 1000, 300000,..."
                          ),
                  },
                ]}
                getValueFromEvent={(event) => {
                  const value = parseFloat(event.target.value);
                  return isNaN(value) ? undefined : value;
                }}
              >
                <Input
                  type="number"
                  placeholder="Giá tiền phải đúng định dạng như : 1000, 300000,..."
                  onKeyDown={(e) =>
                    ["e", "E", "+", "-"].includes(e.key) && e.preventDefault()
                  }
                />
              </Form.Item>
              <Form.Item
                name="discount"
                label="Giảm giá"
                getValueFromEvent={(event) => {
                  const value = parseFloat(event.target.value);
                  return isNaN(value) ? undefined : value;
                }}
                rules={[
                  {
                    required: true,
                    message: "Vui lòng nhập giảm giá!",
                  },
                  {
                    validator: (_, value) =>
                      value == null || (value && +value >= 0 && +value <= 100)
                        ? Promise.resolve()
                        : Promise.reject("Giảm giá phải từ 0% đến 100%"),
                  },
                ]}
              >
                <Input
                  type="number"
                  placeholder="giảm giá quần áo , từ 0% đến 100%"
                  onKeyDown={(e) =>
                    ["e", "E", "+", "-"].includes(e.key) && e.preventDefault()
                  }
                />
              </Form.Item>
              <Form.Item
                name="categoryId"
                label="Danh mục sản phẩm"
                initialValue={listCate[0]?.id}
                rules={[{ required: true }]}
              >
                <Select>
                  {listCate.map((cate) => (
                    <Select.Option value={cate.id}>{cate.name}</Select.Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item
                name="description"
                label="Mô tả chi tiết"
                className="des-field"
                wrapperCol={{ span: 24 }}
              >
                <ReactQuill
                  style={{ height: "300px" }}
                  theme="snow"
                  value={value}
                  onChange={setValue}
                />
              </Form.Item>
            </Col>
            <Col span={10}>
              <Form.List name="classifyClothes">
                {(fields, { add, remove }) => (
                  <div
                    style={{
                      display: "flex",
                      rowGap: 16,
                      flexDirection: "column",
                    }}
                  >
                    {fields.map((field) => (
                      <Card
                        size="small"
                        title={`Phân loại quần áo ${field.name + 1}`}
                        key={field.key}
                        extra={
                          <CloseOutlined
                            onClick={() => {
                              remove(field.name);
                            }}
                          />
                        }
                      >
                        <Form.Item
                          label="Màu sắc"
                          name={[field.name, "color"]}
                          rules={[
                            {
                              required: true,
                              message: "Vui lòng nhập tên màu phù hợp",
                            },
                          ]}
                        >
                          <Input placeholder="Vui lòng nhập tên màu phù hợp" />
                        </Form.Item>

                        <Form.Item
                          label="Danh sách ảnh"
                          name={[field.name, "images"]}
                          valuePropName="fileList"
                          getValueFromEvent={(e) => e?.fileList}
                        >
                          <Upload
                            onChange={async (info) => {
                              await uploadImageClothes(info.file);
                            }}
                            beforeUpload={(file) => {
                              switch (file.type) {
                                case 'image/png':
                                case 'image/jpg':
                                case 'image/jpeg':    
                                    return false
                                default:
                                    toast.error('Đuôi file ảnh phải là .png , .jpg , .jpeg' , toastMSGObject())
                                    return Upload.LIST_IGNORE
                              }
                            }}
                            listType="picture-card"
                          >
                            <Button icon={<UploadOutlined />}>Upload</Button>
                          </Upload>
                        </Form.Item>

                        {/* Nest Form.List */}
                        <Form.Item label="Size và số lượng">
                          <Form.List name={[field.name, "quantities"]}>
                            {(subFields, subOpt) => (
                              <div
                                style={{
                                  display: "flex",
                                  flexDirection: "column",
                                  rowGap: 16,
                                }}
                              >
                                {subFields.map((subField) => (
                                  <Space key={subField.key}>
                                    <Form.Item
                                      noStyle
                                      name={[subField.name, "size"]}
                                      initialValue={"M"}
                                    >
                                      <Input />
                                    </Form.Item>
                                    <Form.Item
                                      noStyle
                                      name={[subField.name, "quantityInStock"]}
                                      getValueFromEvent={(event) => {
                                        const value = parseFloat(
                                          event.target.value
                                        );
                                        return isNaN(value) ? undefined : value;
                                      }}
                                      rules={[
                                        {
                                          required: true,
                                          message:
                                            "Vui lòng nhập số lượng trong kho!",
                                        },
                                        {
                                          validator: (_, value) =>
                                            value &&
                                            +value >= 0 &&
                                            +value <= 100
                                              ? Promise.resolve()
                                              : Promise.reject(
                                                  "Số lượng trong kho chỉ từ 0 đến 999"
                                                ),
                                        },
                                      ]}
                                    >
                                      <Input
                                        type="number"
                                        placeholder="Số lượng trong kho hàng"
                                        onKeyDown={(e) =>
                                          ["e", "E", "+", "-"].includes(
                                            e.key
                                          ) && e.preventDefault()
                                        }
                                      />
                                    </Form.Item>
                                    <CloseOutlined
                                      onClick={() => {
                                        subOpt.remove(subField.name);
                                      }}
                                    />
                                  </Space>
                                ))}
                                <Button
                                  type="dashed"
                                  onClick={() => subOpt.add()}
                                  block
                                >
                                  + Thêm 1 size mới
                                </Button>
                              </div>
                            )}
                          </Form.List>
                        </Form.Item>
                      </Card>
                    ))}

                    <Button type="dashed" onClick={() => add()} block>
                      + Thêm 1 phân loại quần áo mới
                    </Button>
                  </div>
                )}
              </Form.List>
            </Col>
          </Row>

          <Form.Item noStyle shouldUpdate>
            {() => (
              <Typography style={{ marginTop: "30px" }}>
                <pre>{JSON.stringify(newClothesCustome(), null, 2)}</pre>
              </Typography>
            )}
          </Form.Item>
        </Form>
      </Drawer>
    </div>
  );
}
