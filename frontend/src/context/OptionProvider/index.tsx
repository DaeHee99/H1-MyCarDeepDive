import { useReducer, createContext, useMemo } from 'react';
import {
  InitialStateType,
  OptionContextType,
  ActionType,
  OptionProviderProps,
  SET_OPTIONID,
  SET_PACKAGE,
} from './type';

const initialState: InitialStateType = {
  optionId: 0,
  packageOption: false,
};

export const OptionContext = createContext<OptionContextType>({
  ...initialState,
  optionDispatch: () => {},
});

const reducer = (state: InitialStateType, action: ActionType) => {
  switch (action.type) {
    case SET_OPTIONID:
      return { ...state, optionId: action.optionId };
    case SET_PACKAGE:
      return { ...state, packageOption: action.packageOption };
    default:
      throw new Error();
  }
};

const OptionProvider = ({ children }: OptionProviderProps) => {
  const [state, optionDispatch] = useReducer(reducer, initialState);

  const memoValue = useMemo(() => {
    return { ...state, optionDispatch };
  }, [state, optionDispatch]);

  return (
    <OptionContext.Provider value={memoValue}>
      {children}
    </OptionContext.Provider>
  );
};

export default OptionProvider;
