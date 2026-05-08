package com.toolvault.depot_ops_service.exception;

public class AssetNotFoundException extends RuntimeException {
    public AssetNotFoundException(String tag) {
        super("Asset not found: " + tag);
    }
}