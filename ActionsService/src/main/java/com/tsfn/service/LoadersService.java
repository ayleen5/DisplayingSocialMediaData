package com.tsfn.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.tsfn.model.Category;
import com.tsfn.model.Coupon;
import com.tsfn.repository.ActionsRepository;
import com.tsfn.service.exceptions.CouponAlreadyExistsException;
import com.tsfn.service.exceptions.CouponNotAvailableException;
import com.tsfn.service.exceptions.CouponNotFoundException;

@Service
public class LoadersService {

	@Autowired
	private ActionsRepository couponRepository;

	public Coupon getCouponById(int id) {
        return couponRepository.findById(id)
                               .orElseThrow(() -> new CouponNotFoundException("Coupon not found with ID: " + id));
    }

	public Coupon addCoupon(Coupon coupon) {
		// Check if a coupon with the same title already exists for the same company
		if (couponRepository.findByTitleAndCompanyId(coupon.getTitle(), coupon.getCompanyId()).isPresent()) {
			throw new CouponAlreadyExistsException("Coupon with the same title already exists for this company");
		}
		return couponRepository.save(coupon);
	}

	public Coupon updateCoupon(Coupon coupon) {
		// Check if the coupon exists
		if (!couponRepository.existsById(coupon.getId())) {
			throw new CouponNotFoundException("Coupon not found.");
		}
		// Update the coupon
		return couponRepository.save(coupon);
	}

	public void deleteCoupon(int couponId) {
		// Delete the coupon
		if (!couponRepository.existsById(couponId)) {
			throw new CouponNotFoundException("Coupon not found.");
		}
		couponRepository.deleteById(couponId);
	}

	public List<Coupon> getAllCompanyCoupons(int companyId) {
		return couponRepository.findByCompanyId(companyId);
	}

	public List<Coupon> getCompanyCouponsByCategory(int companyId, Category category) {
		return couponRepository.findByCompanyIdAndCategory(companyId, category);
	}

	public List<Coupon> getCompanyCouponsByMaxPrice(int companyId, double maxPrice) {
		return couponRepository.findByCompanyIdAndPriceLessThanEqual(companyId, maxPrice);
	}

	public void purchaseCoupon(int customerId, int couponId) {
		// Check if the coupon exists
		Coupon coupon = couponRepository.findById(couponId)
				.orElseThrow(() -> new CouponNotFoundException("Coupon not found"));

		// Check if the coupon is still valid for purchase
		if (coupon.getEndDate().isBefore(LocalDate.now()) || coupon.getAmount() <= 0) {
			throw new CouponNotAvailableException("Coupon is no longer available for purchase");
		}

	
	}

}
