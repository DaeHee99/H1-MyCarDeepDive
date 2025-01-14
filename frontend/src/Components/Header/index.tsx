import { useLocation } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import { priceToString } from '@/utils';
import Navigation from './Navigation';
import PriceDetailButton from './priceDetailButton';
import ShowQuotationButton from './showQuotation';
import HeaderLogo from './HeaderLogo';
import HeaderTitle from './HeaderTitle';
import DropDownIcon from './DropDownIcon';
import upArrowIcon from '@/assets/icon/up-arrow-icon.svg';
import UnderLine from '../UnderLine';
import { CarContext } from '@/context/CarProvider';
import OptionItem from './OptionItem';

function Header() {
  const location = useLocation();
  const isHome: boolean = location.pathname === '/';
  const isSelectionPage: boolean = location.pathname.startsWith('/select/');
  const [showPriceInfo, setShowPriceInfo] = useState(false);
  const [displayPriceInfo, setDisplayPriceInfo] = useState(false);
  const [timer, setTimer] = useState<NodeJS.Timeout | undefined>(undefined);
  const { carSpec, color, optionData } = useContext(CarContext);

  useEffect(() => {
    if (!showPriceInfo) {
      if (timer) clearTimeout(timer);
      setTimer(undefined);
      setDisplayPriceInfo(false);
      return;
    }

    const newTimer = setTimeout(() => {
      setDisplayPriceInfo(true);
    }, 550);

    setTimer(newTimer);
  }, [showPriceInfo]);

  return (
    <header
      className={`fixed w-full ${
        isSelectionPage
          ? !showPriceInfo
            ? 'shadow-md z-30 h-[120px]'
            : 'shadow-md z-50 h-[322px]'
          : 'h-[92px] z-30'
      } top-0 left-0 transition-height duration-700 ${
        !isHome && 'bg-grey-1000'
      }`}
    >
      <div className='max-w-5xl mx-auto'>
        <div className='flex mt-[33px]'>
          <HeaderLogo />
          <HeaderTitle />
          <DropDownIcon />
        </div>

        {isSelectionPage && (
          <div className='flex justify-between'>
            <Navigation />
            <div className='mt-[10px] flex'>
              <PriceDetailButton
                showPriceInfo={showPriceInfo}
                setShowPriceInfo={setShowPriceInfo}
              />
              <ShowQuotationButton />
            </div>
          </div>
        )}
      </div>
      {displayPriceInfo && isSelectionPage && (
        <>
          <UnderLine margin='mt-4' />
          <div className='max-w-5xl mx-auto'>
            <div className='h-[135px] flex overflow-scroll'>
              <div className='flex my-auto'>
                <div className='w-40 flex flex-col gap-1.5 justify-start font-body4-regular text-grey-300'>
                  <div className='flex gap-4 justify-between'>
                    <p>{carSpec.feature.engine}</p>
                    <p className='font-body4-medium text-grey-100'>
                      {priceToString(carSpec.price)}원
                    </p>
                  </div>
                  <div className='flex gap-4 justify-between'>
                    <p>{carSpec.feature.body}</p>
                  </div>
                  <div className='flex gap-4 justify-between'>
                    <p>{carSpec.feature.drivingSystem}</p>
                  </div>
                </div>

                <div className='h-[94px] my-auto mx-4 border-[0.5px] border-grey-700'></div>

                <div className='w-48 flex flex-col gap-1.5 justify-start font-body4-regular text-grey-300'>
                  <div className='flex gap-4 justify-between'>
                    <p>{color.exteriorColor.name}</p>
                    <p className='font-body4-medium text-grey-100'>
                      {priceToString(color.exteriorColor.price)}원
                    </p>
                  </div>
                  <div className='flex gap-4 justify-between'>
                    <p>{color.interiorColor.name}</p>
                    <p className='font-body4-medium text-grey-100'>
                      {priceToString(color.interiorColor.price)}원
                    </p>
                  </div>
                </div>

                <div className='h-[94px] my-auto mx-4 border-[0.5px] border-grey-700'></div>

                <div className='flex flex-col gap-1.5 justify-start font-body4-regular text-grey-300'>
                  {optionData.map(item => (
                    <OptionItem {...item} key={item.name} />
                  ))}
                </div>
              </div>
            </div>
            <UnderLine margin='mb-4' />
            <div className='flex justify-end'>
              <button className='mr-1'>
                <img
                  src={upArrowIcon}
                  alt='upArrowIcon'
                  onClick={() => setShowPriceInfo(false)}
                />
              </button>
              <p className='font-h1-medium'>
                <span className='text-[24px] text-grey-50'>
                  {priceToString(
                    carSpec.price +
                      color.exteriorColor.price +
                      color.interiorColor.price +
                      optionData.reduce((sum, item) => sum + item.price, 0),
                  )}
                  원
                </span>
              </p>
            </div>
          </div>
        </>
      )}
    </header>
  );
}

export default Header;
