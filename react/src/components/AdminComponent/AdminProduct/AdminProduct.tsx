import { CloseOutlined , DeleteOutlined , EditOutlined } from '@ant-design/icons';
import { Button, Card, Form, Input, Space, Typography ,Select, Radio, Drawer, Row, Col } from 'antd';
import { useState , useRef , useEffect } from 'react';
import './AdminProduct.css'
import { BiPlus } from 'react-icons/bi';
import TableComponent from '../../TableComponent/TableComponent';
import Highlighter from 'react-highlight-words'
import type { InputRef } from 'antd';
import { FilterConfirmProps } from 'antd/es/table/interface';
import { SearchOutlined } from '@ant-design/icons';
import { useQuery } from '@tanstack/react-query';
import { getAllClothes, getClothesById } from '../../../services/clothesService';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';

export default function AdminProduct() {

  const [form] = Form.useForm();
  const [isOpenDrawer , setisOpenDrawer] = useState(false) ;
  const [rowSelected , setRowSelected] = useState<any>({});
  const [value, setValue] = useState('');

  const handleOnCloseDrawer = () => {
    setisOpenDrawer(false);
    form.resetFields();
  }

  // Get all clothes via API

  const fetchGetAllProducts = async () => {
    const res = await getAllClothes();
    const {products , totalCount} = await res?.json();
    return products
  }

  const {data : listProducts , isLoading : isLoadingProducts} = useQuery(['all-product'], fetchGetAllProducts)
  console.log(listProducts)

  // get detail clothes via API

  const fetchGetProductById = async (context : any) => {
    const idClothes = context?.queryKey[1]
    const res = await getClothesById(idClothes);
    return res;
  }

  const {data : product , isSuccess : isSuccessProduct} = useQuery(['all-product',rowSelected.id], fetchGetProductById)
  console.log()

  useEffect(() => {
    if(isSuccessProduct){
      form.setFieldsValue({...product})
    }
  },[isSuccessProduct])

  const handleGetDetailProduct = () => {
    setisOpenDrawer(true);
  }

  // Search and filter Product --------------------------------------------------------------------

  const [searchText, setSearchText] = useState('');
  const [searchedColumn, setSearchedColumn] = useState('');
  const searchInput = useRef<InputRef>(null);

  const handleSearch = (
    selectedKeys: string[],
    confirm: (param?: FilterConfirmProps) => void,
    dataIndex: any,
  ) => {
    confirm();
    setSearchText(selectedKeys[0]);
    setSearchedColumn(dataIndex);
  };

  const handleReset = (clearFilters: () => void) => {
    clearFilters();
    setSearchText('');
  };

  const getColumnSearchProps = (dataIndex: any): any => ({
    filterDropdown: ({ setSelectedKeys, selectedKeys, confirm, clearFilters, close } : any) => (
      <div style={{ padding: 8 }} onKeyDown={(e) => e.stopPropagation()}>
        <Input
          ref={searchInput}
          placeholder={`Search ${dataIndex}`}
          value={selectedKeys[0]}
          onChange={(e) => setSelectedKeys(e.target.value ? [e.target.value] : [])}
          onPressEnter={() => handleSearch(selectedKeys as string[], confirm, dataIndex)}
          style={{ marginBottom: 8, display: 'block' }}
        />
        <Space>
          <Button
            type="primary"
            onClick={() => handleSearch(selectedKeys as string[], confirm, dataIndex)}
            icon={<SearchOutlined />}
            size="small"
            style={{ width: 90 }}
          >
            Search
          </Button>
          <Button
            onClick={() => clearFilters && handleReset(clearFilters)}
            size="small"
            style={{ width: 90 }}
          >
            Reset
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              confirm({ closeDropdown: false });
              setSearchText((selectedKeys as string[])[0]);
              setSearchedColumn(dataIndex);
            }}
          >
            Filter
          </Button>
          <Button
            type="link"
            size="small"
            onClick={() => {
              close();
            }}
          >
            close
          </Button>
        </Space>
      </div>
    ),
    filterIcon: (filtered: boolean) => (
      <SearchOutlined style={{ color: filtered ? '#1677ff' : undefined }} />
    ),
    onFilter: (value : any, record : any) =>
      record[dataIndex]
        .toString()
        .toLowerCase()
        .includes((value as string).toLowerCase()),
    onFilterDropdownOpenChange: (visible : boolean) => {
      if (visible) {
        setTimeout(() => searchInput.current?.select(), 100);
      }
    },
    render: (text : any) =>
      searchedColumn === dataIndex ? (
        <Highlighter
          highlightStyle={{ backgroundColor: '#ffc069', padding: 0 }}
          searchWords={[searchText]}
          autoEscape
          textToHighlight={text ? text.toString() : ''}
        />
      ) : (
        text
      ),
  });

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
        render: (text : string) => <a>{text}</a>,
        width : 400,
        sorter: (a : any,b : any) => a.name.length - b.name.length,
        ...getColumnSearchProps('name')
    },
    {
      title: 'Đơn vị lưu kho',
      dataIndex: 'sku',
      render: (text : string) => <span>{text}</span>,
      sorter: (a : any,b : any) => a.sku.length - b.sku.length,
      ...getColumnSearchProps('sku')
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
            <Button onClick={handleOnCloseDrawer} type="primary">
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
              <Form.Item name="price" label="Price" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item name="discount" label="Discount" rules={[{ required: true }]}>
                <Input />
              </Form.Item>
              <Form.Item name="gender" label="Gender" rules={[{ required: true }]}>
                <Radio.Group >
                  <Radio value={1}>Nam</Radio>
                  <Radio value={2}>Nữ</Radio>
                </Radio.Group>
              </Form.Item>
              <Form.Item name="category" label="Danh mục sản phẩm" rules={[{ required: true }]}>
                <Select
                  defaultValue={'Áo phong'}
                >
                  <Select.Option value={'Áo phong'}>Áo phong</Select.Option>
                  <Select.Option value={'Áo khoát'}>Áo khoát</Select.Option>
                  <Select.Option value={'Áo polo'}>Áo polo</Select.Option>
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

                        <Form.Item label="Images" name={[field.name, 'images']}>
                          <Form.List name={[field.name, 'images']}>
                            {(subFields, subOpt) => (
                              <div style={{ display: 'flex', flexDirection: 'column', rowGap: 16 }}>
                                {subFields.map((subField) => (
                                  <div style={{display:'flex' , alignItems:'center'}} key={subField.key}>
                                    <Form.Item noStyle name={[subField.name]}>
                                      <Input/>
                                    </Form.Item>
                                    <CloseOutlined
                                      style={{marginLeft:"10px"}}
                                      onClick={() => {
                                        subOpt.remove(subField.name);
                                      }}
                                    />
                                  </div>
                                ))}
                                <Button type="dashed" onClick={() => subOpt.add()} block>
                                  + Add extra image url
                                </Button>
                              </div>
                            )}
                          </Form.List>
                        </Form.Item>

                        {/* Nest Form.List */}
                        <Form.Item label="Quantities">
                          <Form.List name={[field.name, 'quantities']}>
                            {(subFields, subOpt) => (
                              <div style={{ display: 'flex', flexDirection: 'column', rowGap: 16 }}>
                                {subFields.map((subField) => (
                                  <Space key={subField.key}>
                                    <Form.Item noStyle name={[subField.name,'size']}>
                                      <Select
                                        defaultValue={'M'}
                                      >
                                        <Select.Option value={'M'}>M</Select.Option>
                                        <Select.Option value={'L'}>L</Select.Option>
                                        <Select.Option value={'XL'}>XL</Select.Option>
                                      </Select>
                                    </Form.Item>
                                    <Form.Item noStyle name={[subField.name,'quantity']}>
                                      <Input placeholder="Số lượng" />
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
                <pre>{JSON.stringify(form.getFieldsValue(), null, 2)}</pre>
              </Typography>
            )}
          </Form.Item>
        </Form>      
      </Drawer>
    </div>
  )
}
