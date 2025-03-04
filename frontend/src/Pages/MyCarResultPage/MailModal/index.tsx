import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { ModalProps } from '@/global/type';
import Button from '@/Components/Button';
import Modal from '@/Components/Modal';
import CloseModal from '@/Components/Modal/CloseModal';
import Toast from '@/Components/Toast';

function MailModal({ showModal, setShowModal }: ModalProps) {
  const { id } = useParams();
  const [email, setEmail] = useState('');
  const [showToast, setShowToast] = useState(false);
  const [success, setSuccess] = useState(true);
  const [toastMessage, setToastMessage] = useState('');

  const emailHandler = ({ target }: React.ChangeEvent<HTMLInputElement>) =>
    setEmail(target.value);

  const setErrorToast = () => {
    setSuccess(false);
    setToastMessage('이메일 전송에 실패했습니다...');
  };

  const setSuccessToast = () => {
    setSuccess(true);
    setToastMessage('해당 이메일 주소로 차량을 전송했습니다!');
  };

  const sendMail = async () => {
    try {
      const res = await fetch(
        `https://api.make-my-car.shop/api/v1/pdf/${id}/email/${email}`,
      );

      if (!res.ok) setErrorToast();
      else {
        const data = await res.json();
        if (!data.data) setErrorToast();
        else setSuccessToast();
      }
    } catch (error: any) {
      setErrorToast();
    } finally {
      setShowToast(true);
    }
  };

  useEffect(() => {
    if (!showModal) setEmail('');
  }, [showModal]);

  return (
    <Modal showModal={showModal} setShowModal={setShowModal}>
      <div className='w-[427px] h-[264px] absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-grey-1000 rounded-xl'>
        <CloseModal />
        <div className='px-8 pt-6'>
          <p className='font-h1-medium text-grey-50'>메일 주소를 알려주세요</p>
          <p className='font-body4-regular text-grey-400 mt-2 mb-8'>
            해당 주소로 만들어진 내 차를 보내드려요.
          </p>
          <input
            type='email'
            className={`w-full mb-8 font-body4-regular px-3 py-[9px] border border-primary rounded-lg ${
              email ? 'text-grey-0' : 'text-grey-600'
            }`}
            placeholder='example@gmail.com'
            value={email}
            onChange={emailHandler}
          />
          <Button
            width='w-full'
            height='h-[46px]'
            variant='primary'
            text='보내기'
            onClick={sendMail}
          />
        </div>
      </div>

      <Toast
        success={success}
        message={toastMessage}
        showToast={showToast}
        setShowToast={setShowToast}
      />
    </Modal>
  );
}

export default MailModal;
