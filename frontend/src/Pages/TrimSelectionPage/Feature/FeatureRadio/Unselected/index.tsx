interface Props {
  name: string;
  value: string;
  onChangeHandler: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

function Unselected({ name, value, onChangeHandler }: Props) {
  return (
    <>
      <input
        id={value}
        type='radio'
        name={name}
        value={value}
        className='hidden'
        onChange={onChangeHandler}
      />
      <label htmlFor={value}>
        <div className='mb-3'>
          <div className='flex justify-center items-center w-[142px] h-10 bg-grey-800 font-body4-regular text-grey-500 rounded-md cursor-pointer'>
            <p>{value}</p>
          </div>
        </div>
      </label>
    </>
  );
}

export default Unselected;
