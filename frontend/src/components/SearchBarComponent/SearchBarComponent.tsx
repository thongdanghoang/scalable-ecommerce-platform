import { AutoComplete, Button, Input } from "antd";
import { useCallback, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { searchClother } from "../../services/clothesService";
import { clothes } from "../../model/ClothesModal";

function useDebounce(effect: Function, dependencies: any[], delay: number) {
  const callback = useCallback(effect, dependencies);

  useEffect(() => {
    const timeout = setTimeout(callback, delay);
    return () => clearTimeout(timeout);
  }, [callback]);
}

async function getAvailableClothes(searchText: string): Promise<clothes[]> {
  let data = await searchClother(searchText);
  if (data) {
    return data.products;
  } else {
    return [];
  }
}

export default function SearchBarComponent() {
  const [searchText, setSearchText] = useState("");
  const [searchOptions, setSearchOptions] = useState([] as { value: string }[]);
  const navigate = useNavigate();

  const handleSearch = async () => {
    if (searchText.trim().length == 0) {
      setSearchOptions([]);
    } else {
      let clothersList = await getAvailableClothes(searchText);
      setSearchOptions(
        clothersList.map((clothes) => ({ value: clothes.name }))
      );
    }
  };

  const handleSubmitSearch = async (search: string = searchText) => {
    navigate("/product", { state: { searchText: search } });
    navigate(0);
  };

  useDebounce(handleSearch, [searchText], 300);
  return (
    <>
      <form
        style={{ width: "100%" }}
        onSubmit={(event) => {
          event.preventDefault();
          handleSubmitSearch();
        }}
      >
        <AutoComplete
          options={searchOptions}
          style={{ width: "80%"}}
          bordered={false}
          onSelect={(selected) => handleSubmitSearch(selected)}
          onSearch={(text) => setSearchText(text)}
          placeholder="Nhập để tìm kiếm sản phẩm"
          size="large"
        />
        <button className="header-search-btn border">
          <i className="header-search-btn-icon fa-solid fa-magnifying-glass"></i>
        </button>
      </form>
    </>
  );
}
