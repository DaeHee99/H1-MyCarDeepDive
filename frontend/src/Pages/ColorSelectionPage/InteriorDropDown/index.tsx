import { CarContext } from '@/context/CarProvider';
import { useContext, useState } from 'react';
import DropDown from '../DropDown';
import DropDownButton from '../DropDown/DropDownButton';
import DropDownContent from '../DropDown/DropDownContent';
import { closeModalHandler } from '@/utils';
import {
  SET_CARSPECID,
  SET_CARSPECPRICE,
  SET_INTERIORCOLOR,
  SET_OPTION_DEFAULT,
  SET_TRIMID,
  SET_TRIMNAME,
} from '@/context/CarProvider/type';

import OtherInteriorItems from '../OtherInteriorItems';
import { getInteriorColorType } from '@/api/color/getInteriorColors';
import ChangerModal from '../ChangerModal';
import { getTrimType } from '@/api/trim/getTrim';

interface Props {
  classifiedInteriorColor: getInteriorColorType | undefined;
  getTrimInfo: getTrimType | undefined;
  setView: React.Dispatch<React.SetStateAction<string>>;
}

function InteriorDropDown({
  classifiedInteriorColor,
  getTrimInfo,
  setView,
}: Props) {
  const [showOtherColor, setShowOtherColor] = useState(false);
  const [wantedOtherColor, setWantedOtherColor] = useState<any>();
  const [wantedOtherColorTrim, setWantedOtherColorTrim] = useState<any>();
  const [showModal, setShowModal] = useState(false);
  const { carDispatch } = useContext(CarContext);

  const clickHandler = ({
    currentTarget,
  }: React.MouseEvent<HTMLButtonElement>) => {
    const dataObject = currentTarget.getAttribute('data-object');
    if (dataObject) {
      const colorInfo = JSON.parse(dataObject);
      setWantedOtherColor(colorInfo);
      const index = getTrimInfo?.data.car_specs.findIndex(
        id => id.trim_id === colorInfo.trim_id,
      );
      if (index === undefined || !getTrimInfo) return;
      const wantedOtherColorTrim = getTrimInfo?.data.car_specs[index];
      setWantedOtherColorTrim(wantedOtherColorTrim);
      setShowModal(true);
    }
  };

  const changeHandler = () => {
    closeModalHandler();
    setView('interial');
    const { color_id, name, img_url, price, choose_rate } = wantedOtherColor;
    const {
      trim_id: trimId,
      trim_name,
      car_spec_id: carSpecId,
      price: carSpecPrice,
    } = wantedOtherColorTrim;

    carDispatch({ type: SET_TRIMID, trimId });
    carDispatch({ type: SET_TRIMNAME, trimName: trim_name });
    carDispatch({ type: SET_CARSPECID, carSpecId: carSpecId });
    carDispatch({ type: SET_CARSPECPRICE, carSpecPrice });

    carDispatch({
      type: SET_INTERIORCOLOR,
      interiorColor: {
        id: color_id,
        name,
        imgUrl: img_url,
        price,
        chooseRate: choose_rate,
      },
    });
    carDispatch({ type: SET_OPTION_DEFAULT });
  };

  return (
    <>
      <DropDown>
        <DropDownButton
          setShowOtherColor={setShowOtherColor}
          showOtherColor={showOtherColor}
          phrase={'다른 내장 색상을 찾고 있나요?'}
        />
        <DropDownContent
          showOtherColor={showOtherColor}
          classifiedExteriorColor={classifiedInteriorColor}
        >
          <OtherInteriorItems
            classifiedInteriorColor={classifiedInteriorColor}
            clickHandler={clickHandler}
            setShowModal={setShowModal}
          />
        </DropDownContent>
      </DropDown>

      <ChangerModal
        wantedOtherColor={wantedOtherColor}
        wantedOtherColorTrim={wantedOtherColorTrim}
        showModal={showModal}
        setShowModal={setShowModal}
        clickHandler={changeHandler}
      />
    </>
  );
}

export default InteriorDropDown;
