import { price } from '@/global/data';
import { QuestionContext } from '@/context/QuestionProvider';
import { useContext } from 'react';
import { SET_BUDGET } from '@/context/QuestionProvider/type';

function Budget() {
  const { myLifeStyle, questionDispatch } = useContext(QuestionContext);
  const { lowestPrice, highestPrice, rangeUnit, priceUnit } = price;
  const currentBudget = `${
    ((Number(myLifeStyle.budget) - lowestPrice) /
      (highestPrice - lowestPrice)) *
    100
  }%`;
  const budgetHandler = ({ target }: React.ChangeEvent<HTMLInputElement>) => {
    questionDispatch({ type: SET_BUDGET, budget: target.value });
  };

  return (
    <>
      <p className='mt-[38px] mb-[34px] font-h2-medium text-grey-50'>
        <span className='text-[24px] leading-[26px] tracking-[-0.2px]'>
          {lowestPrice.toLocaleString('en-US')}
        </span>
        <span className='font-h5-regular'>
          <span className='text-[24px] leading-[26px] tracking-[-0.2px]'>
            {priceUnit}
          </span>
          <span className='text-[22px] leading-[26px] tracking-[-0.2px] mx-2'>
            ~
          </span>
        </span>{' '}
        <span className='font-h2-medium'>
          <span className='text-[24px] leading-[26px] tracking-[-0.2px]'>
            {(+myLifeStyle.budget).toLocaleString('en-US')}
          </span>
        </span>
        <span className='font-h5-regular'>
          <span className='text-[24px] leading-[26px] tracking-[-0.2px]'>
            {priceUnit}
          </span>
        </span>
      </p>

      <div className='w-full h-6 relative flex items-center mb-1.5'>
        <div className='w-6 h-6 border border-grey-500 bg-grey-1000 rounded-full absolute top-0 left-0 z-10' />
        <div className='w-full h-2 rounded-full bg-grey-700 relative'>
          <div
            className='h-2 rounded-full bg-secondary absolute top-0 left-0'
            style={{ width: currentBudget }}
          />
        </div>
        <input
          className='absolute w-full h-6 bg-transparent slider z-20 top-0 left-0'
          type='range'
          min={lowestPrice}
          max={highestPrice}
          step={rangeUnit}
          name={'budget'}
          value={myLifeStyle.budget}
          onChange={budgetHandler}
        />
      </div>
      <div className='flex justify-between font-body4-regular text-grey-400 mb-[52px]'>
        <p>{`${lowestPrice} ${priceUnit}`}</p>
        <p>{`${highestPrice} ${priceUnit}`}</p>
      </div>
    </>
  );
}

export default Budget;
