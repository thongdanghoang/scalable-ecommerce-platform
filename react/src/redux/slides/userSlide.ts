import { createSlice } from '@reduxjs/toolkit'
import type { PayloadAction } from '@reduxjs/toolkit'

export interface User {
  username: string,
  password: string,
  email: string,
  phone?: string,
  address?: string,
  city?: string
}

const initialState: User = {
  username: '',
  password: '',
  email: '',
  phone: '',
  address: '',
  city: ''
}

export const userSlice = createSlice({
  name: 'user',
  initialState,
  reducers: {
    updateUser: (state, action: PayloadAction<User>) => {
      const {
        username = '',
        password = '',
        email = '',
        phone = '',
        address = '',
        city = ''
      } = action.payload
      state.username = username ? username : state.username
      state.password = password ? password : state.password
      state.email = email ? email : state.email
      state.phone = phone ? phone : state.phone
      state.address = address ? address : state.address
      state.city = city ? city : state.city
    },
  },
})

// Action creators are generated for each case reducer function
export const { updateUser } = userSlice.actions

export default userSlice.reducer