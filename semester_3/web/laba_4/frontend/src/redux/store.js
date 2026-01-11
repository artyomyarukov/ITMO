import { createStore } from 'redux';

const initialState = {
    isLoggedIn: false,
    user: null
};

const rootReducer = (state = initialState, action) => {
    console.log("меняем стэйт", action);
    switch (action.type) {
        case 'LOGIN':
            return { ...state, isLoggedIn: true, user: action.payload };
        case 'LOGOUT':
            return { ...state, isLoggedIn: false, user: null };
        default:
            return state;
    }
};

const store = createStore(rootReducer);

export default store;