package com.maurlox21.cryptoalert.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maurlox21.cryptoalert.entity.Alert;
import com.maurlox21.cryptoalert.entity.Cryptocurrency;
import com.maurlox21.cryptoalert.exception.BusinessRuleException;
import com.maurlox21.cryptoalert.exception.EntityNotFoundException;
import com.maurlox21.cryptoalert.repostory.AlertRepository;
import com.maurlox21.cryptoalert.repostory.projection.AlertProjection;

@Service
public class AlertSevice {
    
    @Autowired
    private AlertRepository repository;
    
    @Autowired
    private CryptocurrencyService cryptocurrencyService;

    @Transactional
    public Alert create (Alert alert){

        Cryptocurrency cryptocurrency = null;
        try{
            cryptocurrency = this.cryptocurrencyService.getById(alert.getCryptocurrency().getId());
        } catch(RuntimeException ex){
            throw new BusinessRuleException(ex.getMessage());
        }

        alert.setCryptocurrency(cryptocurrency);
        alert.setTpAlert(alert.getTpAlert().toUpperCase());

        this.repository.save(alert);

        return alert;
    }

    @Transactional(readOnly = true)
    public Page<AlertProjection> getAlertsByIdUser(Long idUser, Pageable pageable){

        return this.repository.findByIdUser(idUser, pageable);
    }

    @Transactional(readOnly = true)
    public Alert getAlertUserbyId(Long idAlert, Long idUser) {
        
        Optional<Alert> optAlert = this.repository.findByIdAndIdUser(idAlert, idUser);

        if(optAlert.isEmpty())
            throw new EntityNotFoundException("Not found alert by id " + idAlert);

        return optAlert.get();
    }

    @Transactional
    public void alter(Long id, Alert alert) {
        
        Alert alertExistent = this.getAlertUserbyId(id, alert.getUser().getId());

        Cryptocurrency cryptocurrency = null;
        try{
            cryptocurrency = this.cryptocurrencyService.getById(alert.getCryptocurrency().getId());
        } catch(RuntimeException ex){
            throw new BusinessRuleException(ex.getMessage());
        }

        alertExistent.setCryptocurrency(cryptocurrency);
        alertExistent.setNrTargetValue(alert.getNrTargetValue());
        alertExistent.setTpAlert(alert.getTpAlert().toUpperCase());

        this.repository.save(alertExistent);
    }

    @Transactional
    public void delete(Long id, Long idUser) {
        
        Alert alertExistent = this.getAlertUserbyId(id, idUser);

        this.repository.delete(alertExistent);
    }

    @Transactional(readOnly = true)
    public Page<Alert> getAlertsByIdCryptocurrenncy(Long idCryptocurrency, Pageable pageable) {
        
        return this.repository.findAllByIdCryptocurrency(idCryptocurrency, pageable);
    }
}
