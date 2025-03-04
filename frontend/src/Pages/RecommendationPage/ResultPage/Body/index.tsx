import { WrapperProps } from '@/global/type';

function Body({ children }: WrapperProps) {
  return <div className='w-[608px] mt-14 mb-9 mx-auto'>{children}</div>;
}

export default Body;
