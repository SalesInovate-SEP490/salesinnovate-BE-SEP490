package fpt.capstone.iOpportunity.services;

import com.google.type.Decimal;
import fpt.capstone.iOpportunity.dto.request.PriceBookDTO;
import fpt.capstone.iOpportunity.dto.request.ProductDTO;
import fpt.capstone.iOpportunity.dto.request.ProductPriceBookDTO;
import fpt.capstone.iOpportunity.dto.response.PageResponse;
import fpt.capstone.iOpportunity.dto.response.PriceBookResponse;
import fpt.capstone.iOpportunity.dto.response.ProductResponse;
import fpt.capstone.iOpportunity.model.Product;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface PriceBookService {
    Long createPriceBook(PriceBookDTO priceBookDTO);

    PageResponse<?> getListPriceBook(int pageNo, int pageSize);

    PriceBookResponse getPriceDetail(long id);

    Boolean patchPriceBook(PriceBookDTO priceBookDTO, long id);

    PageResponse<?> filterPriceBook(Pageable pageable, String[] search);

    Boolean deletePriceBook(long id);

    Boolean addProductToPriceBook(long pricebookId, List<ProductPriceBookDTO> productId);

    Boolean addProductToStandardPriceBook(long pricebookId, long productId, BigDecimal listPrice);

    PageResponse<?> getListProductByPriceBook(int pageNo, int pageSize,long pricebookId);

    // Search product mà chưa có trong pricebook để add và nó đã đươc add vào bên trong standard price book
    List<Product> searchProductToAddPriceBook(long pricebookId,String search);
}
