import { useParams } from 'react-router-dom';
import Step from './Step';

function Title() {
  const { step } = useParams();
  return (
    <div
      className={`mt-12 mx-auto flex justify-between ${step === '1' && 'mb-6'}`}
    >
      {step === '1' ? (
        <p className='font-h2-medium'>
          <span className='text-[22px] leading-[28px]'>나이</span>
          <span className='font-h5-regular'>
            <span className='text-[22px] leading-[28px]'>를 알려주세요.</span>
          </span>
        </p>
      ) : step === '2' ? (
        <p className='font-h5-regular'>
          <span className='text-[22px] leading-[28px]'>유사한 </span>
          <span className='font-h2-medium'>
            <span className='text-[22px] leading-[28px]'>라이프스타일을 </span>
          </span>
          <span className='text-[22px] leaading-[28px] whitespace-pre'>
            {'선택하면\n차량 조합을 추천해 드려요.'}
          </span>
        </p>
      ) : step === 'addition' ? (
        <p className='font-h5-regular mb-2'>
          <span className='text-[24px] leading-[30px]'>당신의 </span>
          <span className='font-h2-medium'>
            <span className='text-[24px] leading-[30px]'>라이프스타일</span>
          </span>
          <span className='text-[24px] leading-[30px]'>을 알려주세요 </span>
        </p>
      ) : (
        ''
      )}
      {step !== 'addition' ? <Step /> : ''}
    </div>
  );
}

export default Title;
