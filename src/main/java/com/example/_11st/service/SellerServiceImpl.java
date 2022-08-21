package com.example._11st.service;

import com.example._11st.domain.Seller;
import com.example._11st.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl {

    private final SellerRepository sellerRepository;
    @Transactional
    public Seller getSeller(Long sellerId){
        return sellerRepository.findById(sellerId)
                .orElseThrow(() -> new EntityNotFoundException("can not find seller by sellerId: " + sellerId));
    }
}
