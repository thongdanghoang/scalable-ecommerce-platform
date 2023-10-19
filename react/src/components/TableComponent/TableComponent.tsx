/* eslint-disable no-unused-vars */
/* eslint-disable react/prop-types */
import { Table } from "antd";
import LoadingComponent from '../../components/LoadingComponent/LoadingComponent'

interface propsTable {
    columns : Array<any>,
    listData : Array<any>,
    isLoading ?: boolean,
    onRow ?: any,
    pageSize ?: number,
    isRowSelection ?: true,
    setListApproveOrder ?: any
}

export default function TableComponent(props : propsTable) {
    const {
        columns = [] , 
        listData = [] , 
        isLoading = false , 
        onRow , 
        pageSize = 6 , 
        isRowSelection = true , 
        setListApproveOrder = ''
    } = props;

    const dataSource = listData?.map(data => ({...data, key : data.id}))

    return (
        <LoadingComponent isloading={isLoading} delay={500}>
            <Table
                rowSelection={isRowSelection && {
                    type: "checkbox",
                    onChange: (selectedRowKeys, selectedRows) => {
                        setListApproveOrder && setListApproveOrder(selectedRows)
                        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
                    },
                }}
                columns={columns}
                dataSource={dataSource}
                onRow={onRow}
                bordered={true}
                pagination={{
                    pageSize,
                }}
                
            />
        </LoadingComponent>
    )
}
