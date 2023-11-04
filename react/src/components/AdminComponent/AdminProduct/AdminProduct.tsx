import { CloseOutlined , DeleteOutlined , EditOutlined } from '@ant-design/icons';
import { Upload , Button, Card, Form, Input, Space, Typography ,Select,Drawer, Row, Col } from 'antd';
import { useState ,useEffect, useMemo } from 'react';
import './AdminProduct.css'
import { BiPlus } from 'react-icons/bi';
import TableComponent from '../../TableComponent/TableComponent';
import { useMutation, useQuery } from '@tanstack/react-query';
import { createNewClothes, getAllClothes, getClothesById, uploadImageClothes } from '../../../services/clothesService';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { UploadOutlined } from '@ant-design/icons';
import { toast } from 'react-toastify';
import { toastMSGObject } from '../../../utils/utils';

export default function AdminProduct() {

  const [form] = Form.useForm();
  const [isOpenDrawer , setisOpenDrawer] = useState(false) ;
  const [rowSelected , setRowSelected] = useState<any>({});
  const [value, setValue] = useState('');
  const [listCate , setListCate] = useState<any[]>([]);

  const handleOnCloseDrawer = () => {
    setisOpenDrawer(false);
    form.resetFields();
    form.setFieldsValue({ 
      classifyClothes: [1]// reset lại form classifyClothes thành 1 cái
    });
  }

  const newClothesCustome = () => {
    const formClothes = form.getFieldsValue();
    const classifyClothesCustome = formClothes.classifyClothes && formClothes.classifyClothes.map((item : any) => ({
      ...item,
      images: item?.images?.map((file : any) => file?.name)
    }))
    return {...formClothes , classifyClothes : classifyClothesCustome}
  }

  // Get all clothes via API

  const fetchGetAllProducts = async () => {
    const res = await getAllClothes();
    const {products , totalCount} = await res?.json();
    console.log(totalCount)
    return products
  }

  const queryAllProducts = useQuery(['all-product'], fetchGetAllProducts )
  const {data : listProducts , isLoading : isLoadingProducts} = queryAllProducts
  console.log(listProducts)

  // get detail clothes via API

  const fetchGetProductById = async (context : any) => {
    const idClothes = context?.queryKey[1]
    const res = await getClothesById(idClothes);
    return res;
  }

  const {data : product , isSuccess : isSuccessProduct} = useQuery(['product-detail',rowSelected.id], fetchGetProductById , { enabled : !!rowSelected.id})

  useEffect(() => {
    if(isSuccessProduct){
      form.setFieldsValue({...product})
    }
  },[isSuccessProduct])

  const handleGetDetailProduct = () => {
    setisOpenDrawer(true);
  }

  // get all categories 

  useEffect(() => {
    fetch('http://localhost:8080/api/products/categories')
      .then(res => res.json())
      .then(data => setListCate(data))
  },[])

  // add new clothes

  const mutationAddClo = useMutation(
    async (data : any) => {
      const res = await createNewClothes(data);
      return res.data
    }
  ) 

  const {data : newClothes , isSuccess : isSuccessNewClothes , isError : isErrorNewClothes} = mutationAddClo;

  useEffect(() => {
    if(isSuccessNewClothes && newClothes){
      toast.success('add success', toastMSGObject())
      handleOnCloseDrawer();
    }else if(isErrorNewClothes){
      toast.error('error add' , toastMSGObject())
    }
  },[isSuccessNewClothes])

  const handleAddNewClothes = () => {
    mutationAddClo.mutate({
      ...newClothesCustome()
    },{
      onSettled : () => {
        queryAllProducts.refetch();
      }
    })
  }

  const renderAction = () => {
    return (
    <div>
        <DeleteOutlined 
          style={{ color: 'red', fontSize: '30px', cursor: 'pointer' }} 

        />
        <EditOutlined 
          style={{ color: 'orange', fontSize: '30px', cursor: 'pointer' }} 
          onClick={handleGetDetailProduct}
        />
    </div>
    )
  }

  const columns = [
    {
      title: 'Tên chi tiết',
      dataIndex: 'name',
      render: (text : string) => <span>{text}</span>,
      width : 400,
      sorter: (a : any,b : any) => a.name.length - b.name.length,
      isSearchProps : true
    },
    {
      title: 'Đơn vị lưu kho',
      dataIndex: 'sku',
      render: (text : string) => <span>{text}</span>,
      sorter: (a : any,b : any) => a.sku.length - b.sku.length,
      isSearchProps : true
    },
    {
      title: 'Giảm giá',
      dataIndex: 'discount',
      render: (text : string) => <span>{text} %</span>,
      sorter: (a : any,b : any) => a.sku - b.sku,
    },
    {
      title: 'Giá tiền',
      dataIndex: 'price',
      sorter: (a : any,b : any) => a.price - b.price,
      filters: [
          {
              text: 'Dưới 50k',
              value: [0,50000],
          },
          {
              text: 'Từ 50k đến 200k',
              value: [50000,200000],
          },
          {
              text: 'Từ 200k đến 500k',
              value: [200000,500000],
          },
          {
              text: 'Từ 500k đến 1000k',
              value: [500000,1000000],
          },
          {
              text: 'Trên 1000k',
              value: [1000000],
          },
        ],
      onFilter: ([start ,end] : number[], record : any) => (end ? (record.price <= end && record.price >= start) : (record.price >= start)),
    },
    {
        title: 'Danh mục',
        dataIndex: 'category',
    },
    {
        title: 'Action',
        dataIndex: 'action',
        render: renderAction
    }
  ];

  return (
    <div id='AdminProduct'>
      <div className="clo-act-btn">
        <Button type="primary" onClick={() => setisOpenDrawer(true)}>
          <BiPlus/>
          Add new clothes
        </Button>
      </div>

      <TableComponent 
          columns={columns} 
          listData={listProducts} 
          isLoading={isLoadingProducts}
          onRow={(record : any, rowIndex : any) => {
              return {
                  onClick : (event : any) => {
                      setRowSelected(record)
                  }
              }
          }}                    
      />

      {/** form add clothes */}
      <Drawer
        title="Create a new account"
        width={1100}
        onClose={handleOnCloseDrawer}
        open={isOpenDrawer}
        bodyStyle={{paddingBottom:"80px"}}
        extra={
          <Space>
            <Button onClick={handleOnCloseDrawer}>Cancel</Button>
            <Button onClick={handleAddNewClothes} type="primary">
              Submit
            </Button>
          </Space>
        }
      >
        <Form
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          form={form}
          name="dynamic_form_complex"
          // style={{ maxWidth: 600 }}
          autoComplete="off"
          initialValues={{ classifyClothes: [{}] }}
        >
          <Row gutter={32}>
            <Col span={14} style={{borderRight: '1px solid #e1e1e1'}}>
              <Form.Item name="name" label="Name" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item name="sku" label="Đơn vị lưu kho" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item 
                name="price" label="Price" 
                rules={[{ required: true }]}
                getValueFromEvent={(event) => {
                  const value = parseFloat(event.target.value);
                  return isNaN(value) ? undefined : value;
                }}
              >
                <Input type='number'/>
              </Form.Item>
              <Form.Item 
                name="discount" label="Discount" 
                rules={[{ required: true }]}
                getValueFromEvent={(event) => {
                  const value = parseFloat(event.target.value);
                  return isNaN(value) ? undefined : value;
                }}
              >
                <Input type='number' />
              </Form.Item>
              <Form.Item name="categoryId" label="Danh mục sản phẩm" initialValue={listCate[0]?.id} rules={[{ required: true }]}>
                <Select>
                  {listCate.map(cate => (
                    <Select.Option value={cate.id}>{cate.name}</Select.Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item 
                  name="description" 
                  label="Detail description"
                  className='des-field'
                  wrapperCol={{ span: 24 }}
              >
                <ReactQuill style={{height:"300px"}} theme="snow" value={value} onChange={setValue} />
              </Form.Item>
            </Col>
            <Col span={10}>
              <Form.List name="classifyClothes">
                {(fields, { add, remove }) => (
                  <div style={{ display: 'flex', rowGap: 16, flexDirection: 'column' }}>
                    {fields.map((field) => (
                      <Card
                        size="small"
                        title={`ClassifyClothes ${field.name + 1}`}
                        key={field.key}
                        extra={
                          <CloseOutlined
                            onClick={() => {
                              remove(field.name);
                            }}
                          />
                        }
                      >
                        <Form.Item label="Color" name={[field.name, 'color']}>
                          <Input/>
                        </Form.Item>

                        <Form.Item label="Images" name={[field.name, 'images']} valuePropName="fileList"
                          getValueFromEvent={(e) => e?.fileList}
                        >
                          <Upload 
                            onChange={async (info) => {
                              const res = await uploadImageClothes(info.file);
                              if(res.success){
                                toast.success(res.message, toastMSGObject({ autoClose : 1000}))
                              }else{
                                toast.error(res.message , toastMSGObject({ autoClose : 1000}))
                              }
                            }}
                            beforeUpload = {(file) => {
                              return false
                            }}
                          >
                            <Button icon={<UploadOutlined />}>Click to Upload</Button>
                          </Upload>
                        </Form.Item>

                        {/* Nest Form.List */}
                        <Form.Item label="Quantities">
                          <Form.List name={[field.name, 'quantities']}>
                            {(subFields, subOpt) => (
                              <div style={{ display: 'flex', flexDirection: 'column', rowGap: 16 }}>
                                {subFields.map((subField) => (
                                  <Space key={subField.key}>
                                    <Form.Item noStyle name={[subField.name,'size']} initialValue={'M'}>
                                      <Select>
                                        <Select.Option value={'M'}>M</Select.Option>
                                        <Select.Option value={'L'}>L</Select.Option>
                                        <Select.Option value={'XL'}>XL</Select.Option>
                                        <Select.Option value={'2XL'}>XL</Select.Option>
                                      </Select>
                                    </Form.Item>
                                    <Form.Item 
                                      noStyle name={[subField.name,'quantity']}
                                      getValueFromEvent={(event) => {
                                        const value = parseFloat(event.target.value);
                                        return isNaN(value) ? undefined : value;
                                      }}
                                    >
                                      <Input type='number' placeholder="Số lượng" />
                                    </Form.Item>
                                    <CloseOutlined
                                      onClick={() => {
                                        subOpt.remove(subField.name);
                                      }}
                                    />
                                  </Space>
                                ))}
                                <Button type="dashed" onClick={() => subOpt.add()} block>
                                  + Add extra size
                                </Button>
                              </div>
                            )}
                          </Form.List>
                        </Form.Item>
                      </Card>
                    ))}

                    <Button type="dashed" onClick={() => add()} block>
                      + Add extra classify clothes
                    </Button>
                  </div>
                )}
              </Form.List>
            </Col>
          </Row>
          
          <Form.Item noStyle shouldUpdate>
            {() => (
              <Typography style={{marginTop:"30px"}} >
                <pre>{JSON.stringify(newClothesCustome(), null, 2)}</pre>
              </Typography>
            )}
          </Form.Item>
        </Form>      
      </Drawer>
    </div>
  )
}
