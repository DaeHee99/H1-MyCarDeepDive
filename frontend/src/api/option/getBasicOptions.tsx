import { useContext } from 'react';
import { CarContext } from '@/context/CarProvider';
import useFetch, { GET } from '@/hooks/useFetch';

export type getBasicOptionsType =
  | {
      status: { code: string; message: string };
      data: {
        basic_option_id: number;
        option_img_url: string;
        option_name: string;
        tag_list: {
          tag_id: number;
          tag_name: string;
        }[];
      }[];
    }
  | undefined;

function getBasicOptions(): getBasicOptionsType {
  const { carSpec } = useContext(CarContext);

  return useFetch({
    method: GET,
    url: `/car-spec/${carSpec.id}/basic-options`,
  });
}

export default getBasicOptions;
