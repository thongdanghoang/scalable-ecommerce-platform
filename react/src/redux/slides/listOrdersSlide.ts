import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'
import { Order } from '../../model/OrderModal'
import { AddressShipping } from '../../model/UserModal'
import { clothesOrder } from '../../model/ClothesModal'
import { calculatePriceFinal } from '../../utils/utils'

interface listOrder {
  ordersUnpaid : Order[]
}

const initialState: listOrder = {
  ordersUnpaid : []
}

export const listOrderSlice = createSlice({
  name: 'listOrder',
  initialState,
  reducers: {
    addOrderToList : (state , action : PayloadAction<Order>) => {
      const newOrder = action.payload 
      const findOrder = state.ordersUnpaid.find(order => order.username === newOrder.username);
      if(!findOrder){
        state.ordersUnpaid.push(newOrder)
      }
    },
    updateListOrder : (state , action : PayloadAction<Order>) => {
      const updatedOrder = action.payload 
      const updatedListOrder = state.ordersUnpaid.map(order => {
        console.log(order.username , updatedOrder.username)
        if(order.username === updatedOrder.username){
          return updatedOrder
        }
        return order
      });
      state.ordersUnpaid = [...updatedListOrder];
    }
  },
})

// Action creators are generated for each case reducer function
export const { addOrderToList , updateListOrder } = listOrderSlice.actions

export default listOrderSlice.reducer