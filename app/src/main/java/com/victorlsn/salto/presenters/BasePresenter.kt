package com.victorlsn.salto.presenters

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<V> {

	var view: V? = null
	val disposable = CompositeDisposable()

	open fun attachView(view: V) {
		this.view = view
	}

	open fun detachView() {
		view = null
	}
}