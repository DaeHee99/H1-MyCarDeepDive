interface TrimInformationProps {
  forYou: string;
  trimName: string;
  price: string;
}

function TrimInformation({ forYou, trimName, price }: TrimInformationProps) {
  return (
    <div className='mt-[25px] mb-[26px] flex flex-col gap-2'>
      <span className='font-body4-regular text-grey-300'>{forYou}</span>
      <span className='font-h2-medium text-black'>{trimName}</span>
      <span className='font-h4-medium text-grey-200'>
        <span className='leading-[24px]'>{price}</span>
        <span className='font-body3-regular'>부터</span>
      </span>
    </div>
  );
}

export default TrimInformation;
