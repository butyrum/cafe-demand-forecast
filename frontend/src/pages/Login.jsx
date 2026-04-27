import React, { useState } from 'react'
import { Coffee, User, Lock, Eye } from 'lucide-react'

export default function Login() {
  return (
    <div className="min-h-screen w-full flex items-center justify-center bg-brand-bg p-4">
      
      <div className="w-full max-w-[440px] p-12 rounded-[40px] glass-card flex flex-col items-center">
        
        {/* Logo superior */}
        <div className="w-16 h-16 bg-card-bg rounded-full flex items-center justify-center shadow-md mb-8 border border-white">
          <Coffee className="w-8 h-8 text-espresso" />
        </div>

        {/* Título com contraste máximo */}
        <h1 className="text-[28px] font-black text-espresso text-center leading-tight mb-14 tracking-tight uppercase">
          Previsão de<br />Demanda com IA
        </h1>

        <form className="w-full flex flex-col gap-6">
          {/* Usuário */}
          <div className="space-y-2">
            <label className="text-sm font-bold text-espresso ml-1">Usuário</label>
            <div className="relative">
              <User className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-espresso/40" />
              <input
                type="text"
                placeholder="User"
                className="w-full h-14 pl-12 bg-input-bg rounded-2xl input-styled text-espresso placeholder:text-espresso/30 focus:outline-none"
              />
            </div>
          </div>

          {/* Senha */}
          <div className="space-y-2">
            <label className="text-sm font-bold text-espresso ml-1">Senha</label>
            <div className="relative">
              <Lock className="absolute left-4 top-1/2 -translate-y-1/2 w-5 h-5 text-espresso/40" />
              <input
                type="password"
                placeholder="Lock"
                className="w-full h-14 pl-12 pr-12 bg-input-bg rounded-2xl input-styled text-espresso placeholder:text-espresso/30 focus:outline-none"
              />
              <Eye className="absolute right-4 top-1/2 -translate-y-1/2 w-5 h-5 text-espresso/40 cursor-pointer" />
            </div>
          </div>

          {/* Botão idêntico à imagem */}
          <button className="w-full h-14 mt-8 bg-btn-main text-white font-bold rounded-2xl shadow-lg hover:brightness-110 transition-all active:scale-[0.98]">
            Acessar
          </button>
        </form>

        {/* Info Demo */}
        <div className="mt-12 text-center text-espresso/60 space-y-1">
          <p className="text-sm font-bold">Demo: admin / 123456</p>
          <p className="text-xs">Sistema inteligente de previsão de demanda</p>
        </div>

      </div>

      <footer className="fixed bottom-6 text-espresso/60 text-xs flex items-center gap-2">
        <Coffee size={14} />
        Café Forecast © 2026
      </footer>
    </div>
  )
}