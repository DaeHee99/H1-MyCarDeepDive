import { useNavigate } from 'react-router-dom';
import Button from '@/Components/Button';
import { useContext } from 'react';
import { CarContext } from '@/context/CarProvider';
import { SET_DEFAULT } from '@/context/CarProvider/type';

function Buttons() {
  const navigation = useNavigate();
  const { carDispatch } = useContext(CarContext);

  const makeCar = () => {
    carDispatch({ type: SET_DEFAULT });
    navigation('/select/trim');
  };

  return (
    <div className='fixed bottom-9 left-0 z-20 w-full flex justify-center gap-2'>
      <Button
        width='w-[300px]'
        height='h-[52px]'
        variant='transparent'
        text='직접 만들래요'
        onClick={makeCar}
      />
      <Button
        width='w-[300px]'
        height='h-[52px]'
        variant='white'
        text='추천받기'
        onClick={() => navigation('/recommend/question/1')}
      />
    </div>
  );
}

export default Buttons;
